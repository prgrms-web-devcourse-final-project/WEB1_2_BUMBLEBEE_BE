package roomit.main.domain.chat.dto;

public record ChatMessageRequest(Long roomId, String sender, String content) {
}