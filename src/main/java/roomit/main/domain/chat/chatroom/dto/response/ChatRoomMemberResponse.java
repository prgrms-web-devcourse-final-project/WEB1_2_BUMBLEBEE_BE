package roomit.main.domain.chat.chatroom.dto.response;

import lombok.Builder;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomMemberResponse(
        Long roomId,
        Long businessId,
        String workplaceName,
        String messageContent,
        Boolean isRead,
        LocalDateTime updatedAt
) implements ChatRoomResponse {

    public ChatRoomMemberResponse(ChatRoom chatRoom, ChatMessage chatMessage, String workplaceName) {
        this(
                chatRoom.getRoomId(),
                chatRoom.getBusiness().getBusinessId(),
                workplaceName,
                chatMessage != null ? chatMessage.getContent() : null,
                chatMessage != null ? chatMessage.getIsRead() : false,
                chatMessage != null ? chatMessage.getTimestamp() : chatRoom.getCreatedAt()
        );
    }

    @Override
    public Long id() {
        return businessId;
    }

    @Override
    public String name() {
        return workplaceName;
    }
}
