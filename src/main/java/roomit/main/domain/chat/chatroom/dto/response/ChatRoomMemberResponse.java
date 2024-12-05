package roomit.main.domain.chat.chatroom.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomMemberResponse(
        Long roomId,
        Long businessId,
        String workplaceName,
        LocalDateTime updatedAt
) implements ChatRoomResponse {
    @Override
    public Long id() {
        return businessId;
    }

    @Override
    public String name() {
        return workplaceName;
    }
}
