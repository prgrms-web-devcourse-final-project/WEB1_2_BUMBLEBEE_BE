package roomit.main.domain.chat.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomit.main.domain.chat.chatmessage.controller.ChatController;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.service.ChatService;
import org.springframework.test.web.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    void testSendMessage() throws Exception {
        // Given
        ChatMessageRequest request = new ChatMessageRequest(1L, "Tester", "Hello, WebSocket!", LocalDateTime.now());
        String requestBody = """
            {
                "roomId": 1,
                "sender": "Tester",
                "content": "Hello, WebSocket!",
                "timestamp": "2024-12-01T12:34:56"
            }
        """;

        // When / Then
        mockMvc.perform(post("/pub/chat.sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Mockito.verify(chatService, Mockito.times(1)).sendMessage(Mockito.any(ChatMessageRequest.class));
    }

    @Test
    void testGetMessages() throws Exception {
        // Given
        Long roomId = 1L;
        List<ChatMessageResponse> responses = List.of(new ChatMessageResponse(1L, roomId, "Tester", "Hello, WebSocket!", LocalDateTime.now()));
        Mockito.when(chatService.getMessagesByRoomId(roomId)).thenReturn(responses);

        // When / Then
        mockMvc.perform(get("/chat/room/{roomId}", roomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello, WebSocket!"));

        Mockito.verify(chatService, Mockito.times(1)).getMessagesByRoomId(roomId);
    }
}
