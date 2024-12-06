package roomit.main.domain.chat.chatmessage.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record ChatMessageRequest(
        Long roomId,
        String sender,
        String content,
        LocalDateTime timestamp,
        String senderType // business or member
        ) {
}