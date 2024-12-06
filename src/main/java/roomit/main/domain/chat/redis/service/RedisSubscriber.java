package roomit.main.domain.chat.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8); // 메시지 내용
        log.info("Redis message received: {}", payload);

        // 정적 WebSocket 경로로 전송
        messagingTemplate.convertAndSend("/sub/chat", payload);
        log.info("Message sent to WebSocket destination: {}", "/sub/chat");
    }
}
