package roomit.main.domain.chat.chatmessage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatroom.dto.ChatRoomDetailsDTO;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.chat.redis.service.RedisPublisher;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private final BusinessRepository businessRepository;
    private final MemberRepository memberRepository;

    @Value("${redis.message.ttl:3600}") // 메시지 TTL 설정
    private int messageTtl;

    public void sendMessage(ChatMessageRequest request) {
        ChatRoomDetailsDTO roomDetails = roomRepository.findRoomDetailsById(request.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        boolean isSenderValid = request.senderType().equals("business") && roomDetails.businessName().equals(request.sender())
                || request.senderType().equals("member") && roomDetails.memberNickName().equals(request.sender());

        if (!isSenderValid) {
            throw new IllegalArgumentException("Sender is not authorized to send messages in this room");
        }

        if (request.timestamp() == null) {
            request = new ChatMessageRequest
                    (request.roomId(), request.sender(), request.content(), LocalDateTime.now(), request.senderType());
        }

        // Redis Pub/Sub 발행
        String topic = "/sub/chat/room/" + request.roomId();
        redisPublisher.publish(topic, request);

        // Redis에 저장
        saveMessageToRedis(request);
    }

    private void saveMessageToRedis(ChatMessageRequest request) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + request.roomId() + ":" + request.timestamp();
        redisTemplate.opsForValue().set(redisKey, request, messageTtl, TimeUnit.SECONDS);
    }

    public void flushMessagesToDatabase(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 키 검색
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys == null || keys.isEmpty()) {
            return;
        }

        // Redis에서 데이터 가져오기
        List<ChatMessageRequest> messages = keys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .map(value -> objectMapper.convertValue(value, ChatMessageRequest.class))
                .toList();

        // MySQL에 저장
        saveMessagesToDatabase(messages);

        // Redis에서 키 삭제
        redisTemplate.delete(keys);
    }

    private void saveMessagesToDatabase(List<ChatMessageRequest> messages) {
        messages.forEach(request -> {
            ChatRoom room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found"));

            ChatMessage message = new ChatMessage(room, request);

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
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        System.out.println(senderType);
        System.out.println(room.getMember().getMemberNickName());
        System.out.println(senderName);
        boolean isAuthorized = (senderType.equals("business") && Objects.equals(room.getBusiness().getBusinessName(), senderName))
                || (senderType.equals("member") && Objects.equals(room.getMember().getMemberNickName(), senderName));

        if (!isAuthorized) {
            throw new IllegalArgumentException("Sender is not authorized to view messages in this room");
        }

        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 임시 데이터 조회
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys != null && !keys.isEmpty()) {
            return keys.stream()
                    .map(key -> redisTemplate.opsForValue().get(key))
                    .filter(Objects::nonNull)
                    .map(value -> objectMapper.convertValue(value, ChatMessageRequest.class))
                    .map(request -> new ChatMessageResponse(request))
                    .toList();
        }

        // Redis 데이터가 없으면 MySQL에서 조회
        return messageRepository.findByRoomId(roomId).stream()
                .map(message -> new ChatMessageResponse(message))
                .toList();
    }

    public void deleteOldMessages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        messageRepository.deleteByTimestampBefore(cutoffDate);
    }
}