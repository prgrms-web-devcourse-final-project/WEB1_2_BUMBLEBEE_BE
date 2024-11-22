//package roomit.web1_2_bumblebee_be.domain.chat.controller;
//
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RestController;
//import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageRequest;
//import roomit.web1_2_bumblebee_be.domain.chat.dto.ChatMessageResponse;
//
//@RestController
//public class ChatController {
//
//    @MessageMapping("/chat.{chatRoomId}")
//    @SendTo("/subscribe/chat")
//    public ChatMessageResponse sendMessage(ChatMessageRequest request, @DestinationVariable Long chatRoomId) {
//        return new ChatMessageResponse(request.username(), request.content());
//    }
//}
