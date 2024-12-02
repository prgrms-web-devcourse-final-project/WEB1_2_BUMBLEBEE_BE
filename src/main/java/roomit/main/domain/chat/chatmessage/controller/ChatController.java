package roomit.main.domain.chat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request) {
        chatService.sendMessage(request); // Redis 발행 + MySQL 저장
    }

    @GetMapping("/chat/room/{roomId}")
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId) {
        return chatService.getMessagesByRoomId(roomId); // MySQL에서 메시지 조회
    }
}
