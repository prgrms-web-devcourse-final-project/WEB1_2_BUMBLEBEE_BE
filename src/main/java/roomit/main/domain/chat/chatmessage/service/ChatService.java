package roomit.main.domain.chat.chatmessage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatmessage.repository.ChatMessageRepository;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;
import roomit.main.domain.chat.chatroom.repositoroy.ChatRoomRepository;
import roomit.main.domain.chat.redis.service.RedisPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

        // TTL 설정
        redisTemplate.expire(redisKey, 60, TimeUnit.MINUTES);
    }

    public void flushMessagesToDatabase(Long roomId) {
        String redisKeyPattern = REDIS_MESSAGE_KEY_PREFIX + roomId + ":*";

        // Redis에서 패턴에 맞는 키 검색
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys == null || keys.isEmpty()) {
            return; // 저장할 데이터 없음
        }

        for (String key : keys) {
            // 중복 방지를 위해 Redis에서 락 설정
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(key + ":lock", "1", 5, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                continue; // 다른 프로세스에서 처리 중인 데이터는 건너뜀
            }

            // Redis에서 메시지 읽기
            ChatMessageRequest request = (ChatMessageRequest) redisTemplate.opsForValue().get(key);
            if (request != null) {
                saveMessageToDatabase(List.of(request));
            }

            // Redis에서 키 삭제
            redisTemplate.delete(key);
            redisTemplate.delete(key + ":lock");
        }
    }


    private void saveMessageToDatabase(List<ChatMessageRequest> messages) {
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
