package roomit.main.domain.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageRequest;
import roomit.main.domain.chat.chatmessage.dto.ChatMessageResponse;
import roomit.main.domain.chat.chatmessage.service.ChatService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class ChatIntegrationTest {
    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testFlushMessagesToDatabase() {
        // Given: Redis에 메시지 저장
        ChatMessageRequest request = new ChatMessageRequest(1L, "user1", "Hello", LocalDateTime.now());
        chatService.sendMessage(request);

        // When: 배치 작업 실행
        chatService.flushMessagesToDatabase(1L);

        // Then: MySQL에 저장 확인
        List<ChatMessageResponse> messages = chatService.getMessagesByRoomId(1L);
        assertThat(messages).isNotEmpty();
        assertThat(messages.get(0).content()).isEqualTo("Hello");
    }
}
