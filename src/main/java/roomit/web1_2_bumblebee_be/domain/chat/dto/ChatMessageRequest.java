package roomit.web1_2_bumblebee_be.domain.chat.dto;

public record ChatMessageRequest(Long roomId, String sender, String content) {
}