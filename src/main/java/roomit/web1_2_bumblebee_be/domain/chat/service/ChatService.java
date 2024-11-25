package roomit.web1_2_bumblebee_be.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageRequest;
import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageResponse;
import roomit.web1_2_bumblebee_be.domain.chat.entity.ChatMessage;
import roomit.web1_2_bumblebee_be.domain.chat.repository.ChatMessageRepository;
import roomit.web1_2_bumblebee_be.domain.chatroom.entity.ChatRoom;
import roomit.web1_2_bumblebee_be.domain.chatroom.repositoroy.ChatRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;

    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        ChatRoom room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        ChatMessage message = new ChatMessage(
                room,
                request.sender(),
                request.content(),
                LocalDateTime.now()
        );

        ChatMessage savedMessage = messageRepository.save(message);

        return new ChatMessageResponse(
                savedMessage.getMessageId(),
                savedMessage.getRoom().getRoomId(),
                savedMessage.getSender(),
                savedMessage.getContent(),
                savedMessage.getTimestamp()
        );
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
