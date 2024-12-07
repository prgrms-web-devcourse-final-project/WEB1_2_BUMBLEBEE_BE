package roomit.main.domain.chat.chatmessage.dto.request;

import roomit.main.domain.chat.chatmessage.entity.SenderType;

import java.time.LocalDateTime;

public record ChatMessageRequest(
        Long roomId,
        String sender,
        String content,
        LocalDateTime timestamp,
        String senderType // business or member
        ) {

        public SenderType getSenderTypeEnum() {
                return SenderType.fromString(this.senderType); // Enum으로 변환
        }

        public ChatMessageRequest withTimestamp(LocalDateTime newTimestamp) {
                return new ChatMessageRequest(this.roomId, this.sender, this.content, newTimestamp, this.senderType);
        }
}