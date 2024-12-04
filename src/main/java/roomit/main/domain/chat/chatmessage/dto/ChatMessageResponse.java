package roomit.main.domain.chat.chatmessage.dto;

import lombok.Builder;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse
        (Long messageId,
         Long roomId,
         String sender,
         String content,
         LocalDateTime timestamp
        ) {

    public ChatMessageResponse(ChatMessageRequest request) {
        this(null, request.roomId(), request.sender(), request.content(), request.timestamp());
    }

    public ChatMessageResponse(ChatMessage message) {
        this(message.getMessageId(), message.getRoom().getRoomId(), message.getSender(), message.getContent(), message.getTimestamp());
    }
}