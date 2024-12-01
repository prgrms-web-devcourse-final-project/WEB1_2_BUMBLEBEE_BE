package roomit.main.domain.chat.chatmessage.dto;

import java.time.LocalDateTime;

public record ChatMessageRequest(Long roomId,
                                 String sender,
                                 String content,
                                 LocalDateTime timestamp
) {
}