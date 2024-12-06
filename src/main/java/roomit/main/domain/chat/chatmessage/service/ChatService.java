package roomit.main.domain.chat.chatmessage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageSaveRequest;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repository.ChatRoomRepository;
import roomit.main.domain.chat.redis.service.RedisPublisher;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.global.error.ErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String REDIS_MESSAGE_KEY_PREFIX = "chat:room:";
    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @Value("${redis.message.ttl:3600}") // 메시지 TTL 설정
    private int messageTtl;

    @Transactional
    public void sendMessage(ChatMessageRequest request) {
        ChatRoom roomDetails = roomRepository.findRoomDetailsById(request.roomId())
                .orElseThrow(ErrorCode.CHATROOM_NOT_FOUND::commonException);

        validateSender(request, roomDetails);

        if (request.timestamp() == null) {
            request = new ChatMessageRequest
                    (request.roomId(), request.sender(), request.content(), LocalDateTime.now(), request.senderType());
        }

        // Redis Pub/Sub 발행
        String topic = "/sub/chat/room/" + request.roomId();
        redisPublisher.publish(topic, request);

        // Redis에 저장
        saveMessageToRedis(new ChatMessageSaveRequest(request));
    }

    private void validateSender(ChatMessageRequest request, ChatRoom roomDetails) {
        String businessName = roomDetails.getBusiness().getBusinessName();
        String memberNickName = roomDetails.getMember().getMemberNickName();

        boolean isSenderValid = (request.senderType().equals("business") && businessName.equals(request.sender()))
                                || (request.senderType().equals("member") && memberNickName.equals(request.sender()));


        if (!isSenderValid) {
            throw ErrorCode.CHAT_NOT_AUTHORIZED.commonException();
        }
    }

    private void saveMessageToRedis(ChatMessageSaveRequest message) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + message.roomId() + ":messages"; // Key 수정
        String unreadKey = redisKey + ":unread";

        redisTemplate.opsForZSet().add(redisKey, message, message.timestamp().toEpochSecond(ZoneOffset.UTC));
        redisTemplate.expire(redisKey, Duration.ofSeconds(messageTtl)); // TTL 설정

        redisTemplate.opsForHash().increment(unreadKey, message.sender(), 1);
        redisTemplate.expire(unreadKey, Duration.ofSeconds(messageTtl)); // TTL 설정

        log.info("Saved message to ZSet: key={}, message={}, score={}", redisKey, message, message.timestamp().toEpochSecond(ZoneOffset.UTC));
        log.info("Incremented unread count in Hash: key={}, field={}, value=1", unreadKey, message.sender());
    }


    public void flushMessagesToDatabase(Long roomId) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + roomId + ":messages";

        log.info("Flushing messages to database: key={}", redisKey);

        Set<Object> sortedMessages = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        log.info("Fetched ZSet data from Redis: key={}, messages={}", redisKey, sortedMessages);

        if (sortedMessages == null || sortedMessages.isEmpty()) {
            log.info("No messages to flush: key={}", redisKey);
            return;
        }

        List<ChatMessageSaveRequest> messages = sortedMessages.stream()
                .map(value -> objectMapper.convertValue(value, ChatMessageSaveRequest.class))
                .toList();

        saveMessagesToDatabase(messages);
        log.info("Saved messages to MySQL: messages={}", messages);

        redisTemplate.delete(redisKey);
        log.info("Deleted ZSet data from Redis: key={}", redisKey);
    }



    private void saveMessagesToDatabase(List<ChatMessageSaveRequest> requests) {
        requests.forEach(request -> {
                    // 메시지 읽음 상태 업데이트 (Redis의 읽음 상태 반영)
                    ChatRoom chatRoom = roomRepository.findById(request.roomId())
                            .orElseThrow(ErrorCode.CHATROOM_NOT_FOUND::commonException);
                    ChatMessage message = new ChatMessage(chatRoom, request);
                    messageRepository.save(message);
                }
        );
    }

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId, CustomMemberDetails memberDetails, CustomBusinessDetails businessDetails) {
        String senderName;
        String senderType;

        if (memberDetails != null) {
            senderName = memberDetails.getName();
            senderType = "member";
        } else if (businessDetails != null) {
            senderName = businessDetails.getName();
            senderType = "business";
        } else {
            senderName = null;
            throw ErrorCode.CHAT_NOT_AUTHORIZED.commonException();
        }

        ChatRoom room = roomRepository.findRoomDetailsById(roomId)
                .orElseThrow(ErrorCode.CHATROOM_NOT_FOUND::commonException);

        validateAuthorization(senderType, room, senderName);

        String redisKey = REDIS_MESSAGE_KEY_PREFIX + roomId + ":messages" ;
        String unreadKey = redisKey + ":unread";

        Set<Object> sortedMessages = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        CopyOnWriteArrayList<ChatMessageResponse> combinedList = new CopyOnWriteArrayList<>();

        if (sortedMessages != null) {
            // Redis 데이터 처리
            List<ChatMessageResponse> redisMessages = sortedMessages.stream()
                    .map(value -> objectMapper.convertValue(value, ChatMessageSaveRequest.class)) // ChatMessageSaveRequest로 역직렬화
                    .map(request -> {
                        ChatRoom chatRoom = roomRepository.findById(request.roomId())
                                .orElseThrow(ErrorCode.CHATROOM_NOT_FOUND::commonException);
                        ChatMessage message = new ChatMessage(chatRoom, request);
                        boolean messageRead = isMessageRead(roomId, message.getSender(), senderName);

                        // 읽음 상태 업데이트
                        if (!messageRead) {
                            redisTemplate.opsForHash().delete(unreadKey, message.getSender());
                            log.info("Marked message as read for sender: {}", message.getSender());
                        }

                        return new ChatMessageResponse(message, true); // 읽음 상태를 true로 설정
                    })
                    .toList();

            // Redis 메시지를 CopyOnWriteArrayList에 추가
            combinedList.addAll(redisMessages);
            log.info("Fetched messages from Redis for roomId {}: {}", roomId, redisMessages);
        } else {
            log.info("No Redis key found for roomId {}", roomId);
        }

        // MySQL에서 데이터 조회
        List<ChatMessageResponse> mysqlMessages = messageRepository.findByRoomId(roomId).stream()
                .map(message -> {
                    // 메시지 읽음 상태 확인
                    if (!message.getSender().equals(senderName)) {
                        // 메시지를 읽음으로 설정
                        message.markAsRead(); // ChatMessage 엔티티에 읽음 상태를 변경하는 메서드
                        messageRepository.save(message); // 변경 사항 저장
                        log.info("Marked MySQL message as read for sender: {}", message.getSender());
                    }

                    return new ChatMessageResponse(message, true); // 읽음 상태를 true로 설정
                })
                .toList();

        // MySQL 메시지를 CopyOnWriteArrayList에 추가
        combinedList.addAll(mysqlMessages);
        log.info("Fetched messages from MySQL for roomId {}: {}", roomId, mysqlMessages);

        // 시간순으로 정렬 (최신 메시지가 마지막으로)
        combinedList.sort(Comparator.comparing(ChatMessageResponse::timestamp));

        // Combined 결과 반환
        return combinedList;
    }

    private boolean isMessageRead(Long roomId, String senderId, String currentUserId) {
        // sender가 현재 사용자라면 메시지를 읽은 것으로 간주
        if (senderId.equals(currentUserId)) {
            return true;
        }

        String unreadKey = REDIS_MESSAGE_KEY_PREFIX + roomId + ":messages" + ":unread";
        return redisTemplate.opsForHash().get(unreadKey, senderId) == null;
    }

    private void validateAuthorization(String senderType, ChatRoom room, String senderName) {
        boolean isAuthorized = (senderType.equals("business") && Objects.equals(room.getBusiness().getBusinessName(), senderName))
                || (senderType.equals("member") && Objects.equals(room.getMember().getMemberNickName(), senderName));

        if (!isAuthorized) {
            throw new IllegalArgumentException("Sender is not authorized to view messages in this room");
        }
    }

    public void deleteOldMessages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        messageRepository.deleteByTimestampBefore(cutoffDate);
    }

    public void removeExpiredMessages(Long roomId) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + roomId + ":messages"; // 변경된 Key
        double cutoffTime = LocalDateTime.now().minusSeconds(messageTtl).toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, cutoffTime);
        log.info("Expired messages removed for key={}, roomId={}", redisKey, roomId);
    }


    public void removeExpiredMessagesForAllRooms() {
        Set<String> roomKeys = redisTemplate.keys(REDIS_MESSAGE_KEY_PREFIX + "*");
        if (roomKeys == null || roomKeys.isEmpty()) {
            return;
        }

        for (String roomKey : roomKeys) {
            String roomIdStr = roomKey.replace(REDIS_MESSAGE_KEY_PREFIX, "").split(":")[0];
            try {
                Long roomId = Long.valueOf(roomIdStr);
                removeExpiredMessages(roomId);
            } catch (NumberFormatException e) {
                log.warn("Invalid room key format: {}", roomKey, e);
            }
        }
    }

    @Scheduled(fixedRateString = "120000") // Flush 이후 실행
    public void scheduleRemoveExpiredMessages() {
        removeExpiredMessagesForAllRooms();
    }

}