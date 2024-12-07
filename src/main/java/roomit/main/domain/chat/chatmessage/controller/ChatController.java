package roomit.main.domain.chat.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.chat.chatmessage.dto.request.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.response.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.service.ChatService;
import roomit.main.domain.member.dto.CustomMemberDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request) {
        chatService.sendMessage(request); // Redis 발행 + MySQL 저장
    }

    @GetMapping("/api/v1/chat/room/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId,
                                                 @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                 @AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        return chatService.getMessagesByRoomId(roomId, customMemberDetails, customBusinessDetails); // MySQL에서 메시지 조회
    }
}
