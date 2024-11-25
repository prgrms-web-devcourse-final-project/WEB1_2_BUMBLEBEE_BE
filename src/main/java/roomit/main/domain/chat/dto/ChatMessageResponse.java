package roomit.main.domain.chat.dto;

import java.time.LocalDateTime;

public record ChatMessageResponse(Long messageId, Long roomId, String sender, String content, LocalDateTime timestamp) {
}