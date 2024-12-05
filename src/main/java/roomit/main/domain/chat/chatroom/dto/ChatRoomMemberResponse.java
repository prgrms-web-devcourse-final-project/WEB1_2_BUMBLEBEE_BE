package roomit.main.domain.chat.chatroom.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomMemberResponse(
        Long roomId,
        Long businessId,
        LocalDateTime updatedAt
) implements ChatRoomResponse {
    @Override
    public Long id() {
        return businessId;
    }
}
