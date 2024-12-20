//package roomit.main.domain.chat;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//class WebSocketTest {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Test
//    void testWebSocketMessaging() {
//        String destination = "/sub/chat/room/1";
//        String message = "Hello, WebSocket!";
//
//        // WebSocket 메시지 전송
//        assertDoesNotThrow(() -> messagingTemplate.convertAndSend(destination, message));
//    }
//}