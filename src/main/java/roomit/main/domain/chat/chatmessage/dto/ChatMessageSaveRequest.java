package roomit.main.domain.chat.chatmessage.dto;

import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatMessageSaveRequest (
        Long roomId,
        String sender,
        String content,
        LocalDateTime timestamp,
        String senderType // business or member
) {
    public ChatMessageSaveRequest(ChatMessageRequest request) {
        this(
                request.roomId(),
                request.sender(),
                request.content(),
                request.timestamp(),
                request.senderType()
        );
    }
}