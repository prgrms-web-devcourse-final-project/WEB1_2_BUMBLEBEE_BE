package roomit.main.domain.chat.chatmessage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.chat.redis.service.RedisPublisher;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        saveMessageToRedis(new ChatMessage(roomDetails, request));
    }

    private void validateSender(ChatMessageRequest request, ChatRoom roomDetails) {
        boolean isSenderValid = (request.senderType().equals("business") && roomDetails.getBusiness().getBusinessName().equals(request.sender()))
                || (request.senderType().equals("member") && roomDetails.getBusiness().getBusinessName().equals(request.sender()));

        if (!isSenderValid) {
            throw ErrorCode.CHAT_NOT_AUTHORIZED.commonException();
        }
    }

    private void saveMessageToRedis(ChatMessage message) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + message.getRoom().getRoomId();
        String unreadKey = redisKey + ":unread";

        redisTemplate.opsForZSet().add(redisKey, message, message.getTimestamp().toEpochSecond(ZoneOffset.UTC));

        // 읽지 않은 상태로 저장
        redisTemplate.opsForHash().increment(unreadKey, message.getSender(), 1);
    }

    public void flushMessagesToDatabase(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 키 검색
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys == null || keys.isEmpty()) {
            return;
        }

        // Redis에서 데이터 가져오기
        List<ChatMessage> messages = keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .map(value -> objectMapper.convertValue(value, ChatMessage.class))
                .toList();

        // MySQL에 저장
        saveMessagesToDatabase(messages);

        // Redis에서 키 삭제
        redisTemplate.delete(keys);
    }

    private void saveMessagesToDatabase(List<ChatMessage> messages) {
        messages.forEach(message -> {
            // 메시지 읽음 상태 업데이트 (Redis의 읽음 상태 반영)
            boolean isRead = isMessageRead(message.getRoom().getRoomId(), message.getSender());
            message.changeRead(isRead); // 읽음 상태를 업데이트

            // 메시지를 MySQL에 저장
            messageRepository.save(message);
        });
    }

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId, CustomMemberDetails memberDetails, CustomBusinessDetails businessDetails) {
        String senderName = null;
        String senderType = null;

        if (memberDetails != null) {
            senderName = memberDetails.getName();
            senderType = "member";
        } else if (businessDetails != null) {
            senderName = businessDetails.getName();
            senderType = "business";
        }

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(ErrorCode.CHATROOM_NOT_FOUND::commonException);

        validateAuthorization(senderType, room, senderName);

        String redisKey = REDIS_MESSAGE_KEY_PREFIX + roomId;

        Set<Object> sortedMessages = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        if (sortedMessages != null) {
            return sortedMessages.stream()
                    .map(value -> objectMapper.convertValue(value, ChatMessage.class))
                    .map(message -> {
                        boolean messageRead = isMessageRead(roomId, message.getSender());
                        return new ChatMessageResponse(message, messageRead);
                    })
                    .toList();
        }

        // Redis 데이터가 없으면 MySQL에서 조회
        return messageRepository.findByRoomId(roomId).stream()
                .map(ChatMessageResponse::new)
                .toList();
    }

    private boolean isMessageRead(Long roomId, String senderId) {
        String unreadKey = REDIS_MESSAGE_KEY_PREFIX + roomId + ":unread";
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
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + roomId;
        double cutoffTime = LocalDateTime.now().minusSeconds(messageTtl).toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, cutoffTime);
        log.info("Expired messages removed for roomId: {}", roomId);
    }

    public void removeExpiredMessagesForAllRooms() {
        Set<String> roomKeys = redisTemplate.keys(REDIS_MESSAGE_KEY_PREFIX + "*");
        if (roomKeys == null || roomKeys.isEmpty()) {
            return;
        }

        for (String roomKey : roomKeys) {
            Long roomId = Long.valueOf(roomKey.replace(REDIS_MESSAGE_KEY_PREFIX, ""));
            removeExpiredMessages(roomId);
        }
    }

    @Scheduled(fixedRateString = "${redis.message.cleanup.interval:60000}")
    public void scheduleRemoveExpiredMessages() {
        removeExpiredMessagesForAllRooms();
    }
}