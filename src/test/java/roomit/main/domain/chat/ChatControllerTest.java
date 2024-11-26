//package roomit.main.domain.chat;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import roomit.main.domain.chat.controller.ChatController;
//import roomit.main.domain.chat.dto.ChatMessageRequest;
//import roomit.main.domain.chat.service.ChatService;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ChatController.class)
//class ChatControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ChatService chatService;
//
//    @Test
//    void testPublishMessage() throws Exception {
//        // 요청 객체 생성
//        ChatMessageRequest request = new ChatMessageRequest(1L, "user1", "Hello, WebSocket!");
//
//        // Mock 동작 설정
//        Mockito.doNothing().when(chatService).sendMessage(any(ChatMessageRequest.class));
//
//        // MockMvc로 POST 요청 수행
//        mockMvc.perform(post("/api/chat/publish")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk()); // 응답 상태 코드가 200인지 확인
//
//        // ChatService의 sendMessage 메서드가 호출되었는지 검증
//        Mockito.verify(chatService, Mockito.times(1)).sendMessage(any(ChatMessageRequest.class));
//    }
//}
