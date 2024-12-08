package roomit.main.domain.chat.chatmessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomit.main.domain.chat.chatmessage.dto.request.ChatMessageSaveRequest;
import roomit.main.domain.chat.chatmessage.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
         Long roomId,
         String sender,
         String content,
         Boolean isRead,
         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp
        ) {

    public ChatMessageResponse(ChatMessage chatMessage) {
        this(
                chatMessage.getRoom().getRoomId(),
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getIsRead(),
                chatMessage.getTimestamp()
        );
    }

    public ChatMessageResponse(Long roomId, ChatMessageSaveRequest request, boolean isRead) {
        this(
                roomId,
                request.sender(),
                request.content(),
                isRead,
                request.timestamp()
        );
    }
}