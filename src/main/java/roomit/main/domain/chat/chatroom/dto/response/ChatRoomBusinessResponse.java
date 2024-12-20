package roomit.main.domain.chat.chatroom.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;
import roomit.main.domain.chat.chatroom.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomBusinessResponse(
        Long roomId,
        Long memberId,
        String memberNickname,
        String messageContent,
        Boolean isRead,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAt
) implements ChatRoomResponse {

    public ChatRoomBusinessResponse(ChatRoom chatRoom, ChatMessage chatMessage) {
        this(
                chatRoom.getRoomId(),
                chatRoom.getMember().getMemberId(),
                chatRoom.getMember().getMemberNickName(),
                chatMessage != null ? chatMessage.getContent() : null,
                chatMessage != null ? chatMessage.getIsRead() : true,
                chatMessage != null ? chatMessage.getTimestamp() : chatRoom.getCreatedAt()
        );
    }

    @Override
    public Long id() {
        return memberId;
    }

    @Override
    public String name() {
        return memberNickname;
    }
}
