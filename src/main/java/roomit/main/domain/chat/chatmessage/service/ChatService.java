package roomit.main.domain.chat.chatmessage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.chat.redis.service.RedisLuaService;
import roomit.main.domain.chat.redis.service.RedisPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final RedisLuaService redisLuaService;
    private final ObjectMapper objectMapper;

    @Value("${redis.message.ttl:3600}") // TTL 환경 변수로 관리
    private int messageTtl;

    private static final String REDIS_MESSAGE_KEY_PREFIX = "chat:room:";

    public void sendMessage(ChatMessageRequest request) {
        // 메시지 타임스탬프 관리
        if (request.timestamp() == null) {
            request = new ChatMessageRequest(request.roomId(), request.sender(), request.content(), LocalDateTime.now());
        }

        // Redis Pub/Sub 발행
        String topic = "/sub/chat/room/" + request.roomId();
        redisPublisher.publish(topic, request);

        // Redis에 임시 저장
        saveMessageToRedis(request);
    }

    private void saveMessageToRedis(ChatMessageRequest request) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + request.roomId() + ":" + request.timestamp();
        redisTemplate.opsForValue().set(redisKey, request);

        // TTL 설정
        redisTemplate.expire(redisKey, messageTtl, TimeUnit.SECONDS);
    }

    public void flushMessagesToDatabase(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis Lua 스크립트로 키 및 데이터 가져오기
        List<Object> keysAndValues = redisLuaService.getKeysAndValues(redisKeyPattern);

        List<ChatMessageRequest> messages = keysAndValues.stream()
                .map(value -> objectMapper.convertValue(((List<?>) value).get(1), ChatMessageRequest.class))
                .filter(Objects::nonNull)
                .toList();

        // MySQL에 저장
        saveMessagesToDatabase(messages);

        // Lua 스크립트를 사용해 Redis 키 삭제
        redisLuaService.deleteKeys(redisKeyPattern);
    }

    private void saveMessagesToDatabase(List<ChatMessageRequest> messages) {
        messages.forEach(request -> {
            ChatRoom room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found"));

            ChatMessage message = new ChatMessage(
                    room,
                    request.sender(),
                    request.content(),
                    request.timestamp()
            );
            messageRepository.save(message);
        });
    }

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 임시 데이터 조회
        List<Object> keysAndValues = redisLuaService.getKeysAndValues(redisKeyPattern);
        if (keysAndValues != null && !keysAndValues.isEmpty()) {
            return keysAndValues.stream()
                    .map(value -> objectMapper.convertValue(((List<?>) value).get(1), ChatMessageRequest.class))
                    .filter(Objects::nonNull)
                    .map(request -> new ChatMessageResponse(
                            null, // MySQL 저장 전이므로 ID 없음
                            request.roomId(),
                            request.sender(),
                            request.content(),
                            request.timestamp()
                    ))
                    .toList();
        }

        // Redis 데이터가 없으면 MySQL에서 조회
        return messageRepository.findByRoom_RoomIdOrderByTimestamp(roomId).stream()
                .map(message -> new ChatMessageResponse(
                        message.getMessageId(),
                        message.getRoom().getRoomId(),
                        message.getSender(),
                        message.getContent(),
                        message.getTimestamp()
                ))
                .toList();
    }

    public void deleteOldMessages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        messageRepository.deleteByTimestampBefore(cutoffDate);
    }
}
