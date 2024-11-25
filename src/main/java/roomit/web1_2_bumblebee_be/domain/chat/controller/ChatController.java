package roomit.web1_2_bumblebee_be.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageRequest;
import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageResponse;
import roomit.web1_2_bumblebee_be.domain.chat.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request) {
        ChatMessageResponse response = chatService.saveMessage(request);
        messagingTemplate.convertAndSend("/topic/" + response.roomId(), response);
    }

    @GetMapping("/chat/room/{roomId}")
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId) {
        return chatService.getMessagesByRoomId(roomId);
    }
}
