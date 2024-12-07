package roomit.main.domain.chat.chatroom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface ChatRoomResponse {
    Long roomId();
    Long id();
    String name();
    String messageContent();
    Boolean isRead();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAt();
}
