package roomit.main.domain.chat.chatroom.dto.response;

import java.time.LocalDateTime;

public interface ChatRoomResponse {
    Long roomId();
    Long id();
    String name();
    LocalDateTime updatedAt();
}
