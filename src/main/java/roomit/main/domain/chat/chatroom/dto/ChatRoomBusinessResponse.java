package roomit.main.domain.chat.chatroom.dto;

import java.time.LocalDateTime;

public record ChatRoomBusinessResponse(
        Long roomId,
        Long memberId,
        LocalDateTime updatedAt
) implements ChatRoomResponse {
    @Override
    public Long id() {
        return memberId;
    }
}
