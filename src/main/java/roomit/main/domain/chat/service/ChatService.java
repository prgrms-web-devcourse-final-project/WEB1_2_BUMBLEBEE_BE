package roomit.main.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.main.domain.chat.dto.ChatMessageRequest;
import roomit.main.domain.chat.dto.ChatMessageResponse;
import roomit.main.domain.chat.entity.ChatMessage;
import roomit.main.domain.chat.repository.ChatMessageRepository;
import roomit.main.domain.chatroom.entity.ChatRoom;
import roomit.main.domain.chatroom.repositoroy.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;

    public void sendMessage(ChatMessageRequest request) {

        System.out.println("Sending message to /sub/chat/room/" + request.roomId());
        System.out.println("Message content: " + request);

        // Redis Pub/Sub 발행
        String topic = "/sub/chat/room/" + request.roomId();
        redisPublisher.publish(topic, request);

        // MySQL에 저장
        saveMessageToDatabase(request);
    }

    private void saveMessageToDatabase(ChatMessageRequest request) {
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

    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        return messageRepository.findByRoom_RoomId(roomId).stream()
                .map(message -> new ChatMessageResponse(
                        message.getMessageId(),
                        message.getRoom().getRoomId(),
                        message.getSender(),
                        message.getContent(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    public void deleteOldMessages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        messageRepository.deleteByTimestampBefore(cutoffDate);
    }

}
