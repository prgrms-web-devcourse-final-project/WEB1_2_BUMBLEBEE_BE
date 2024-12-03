package roomit.main.domain.chat.chatmessage.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse
        (Long messageId,
         Long roomId,
         String sender,
         String content,
         LocalDateTime timestamp
        ) {
}