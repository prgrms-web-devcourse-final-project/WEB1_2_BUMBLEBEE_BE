package roomit.main.domain.chat.chatroom.dto.response;

import java.time.LocalDateTime;

public record ChatRoomBusinessResponse(
        Long roomId,
        Long memberId,
        String memberNickname,
        LocalDateTime updatedAt
) implements ChatRoomResponse {
    @Override
    public Long id() {
        return memberId;
    }

    @Override
    public String name() {
        return memberNickname;
    }
}
