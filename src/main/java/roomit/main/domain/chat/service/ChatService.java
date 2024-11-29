package roomit.main.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import roomit.main.domain.chat.dto.ChatMessageRequest;
import roomit.main.domain.chat.dto.ChatMessageResponse;
import roomit.main.domain.chat.entity.ChatMessage;
import roomit.main.domain.chat.repository.ChatMessageRepository;
import roomit.main.domain.chatroom.entity.ChatRoom;
import roomit.main.domain.chatroom.repositoroy.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;

    private static final String REDIS_MESSAGE_KEY_PREFIX = "chat:room:";

    public void sendMessage(ChatMessageRequest request) {
        // Redis Pub/Sub 발행
        String topic = "/sub/chat/room/" + request.roomId();
        redisPublisher.publish(topic, request);

        // Redis에 임시 저장
        saveMessageToRedis(request);
    }

    private void saveMessageToRedis(ChatMessageRequest request) {
        String redisKey = REDIS_MESSAGE_KEY_PREFIX + request.roomId() + ":" + request.timestamp();
        redisTemplate.opsForValue().set(redisKey, request);

        // TTL 설정 (예: 30분 후 만료)
        redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
    }

    public void flushMessagesToDatabase(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 패턴에 맞는 키 검색
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys == null || keys.isEmpty()) {
            return; // 저장할 데이터 없음
        }

        // Redis에서 메시지 읽기 및 MySQL에 저장
        List<ChatMessageRequest> messages = keys.stream()
                .map(key -> (ChatMessageRequest) redisTemplate.opsForValue().get(key))
                .filter(Objects::nonNull)
                .toList();

        // MySQL에 저장
        saveMessagesToDatabase(messages);

        // Redis에서 키 삭제
        redisTemplate.delete(keys);
    }


    private void saveMessagesToDatabase(List<ChatMessageRequest> messages) {
        for (ChatMessageRequest request : messages) {
            ChatRoom room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found"));

            ChatMessage message = new ChatMessage(
                    room,
                    request.sender(),
                    request.content(),
                    LocalDateTime.now()
            );
            messageRepository.save(message);
        }
    }

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        return messageRepository.findByRoom_RoomId(roomId).stream()
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
