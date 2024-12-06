package roomit.main.domain.chat.chatmessage.dto;

import roomit.main.domain.chat.chatmessage.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
         Long roomId,
         String sender,
         String content,
         Boolean isRead,
         LocalDateTime timestamp
        ) {

    public ChatMessageResponse(ChatMessage chatMessage) {
        this(
                chatMessage.getRoom().getRoomId(),
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getIsRead(),
                chatMessage.getTimestamp()
        );
    }

    public ChatMessageResponse(ChatMessage message, boolean isRead) {
        this(
                message.getRoom().getRoomId(),
                message.getSender(),
                message.getContent(),
                isRead,
                message.getTimestamp()
        );
    }
}