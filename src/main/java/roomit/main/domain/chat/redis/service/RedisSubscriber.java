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
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8); // 실제 채널 이름
        log.info("Redis message received on channel: {}, payload: {}", channel, payload);

        // 실제 WebSocket 구독 경로로 전송
        messagingTemplate.convertAndSend(channel, payload);
        log.info("Message sent to WebSocket destination: {}", channel);
    }
}
