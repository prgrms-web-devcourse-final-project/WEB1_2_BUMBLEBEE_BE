package roomit.main.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(pattern); // 주제 확인
        String payload = new String(message.getBody()); // 메시지 내용
        messagingTemplate.convertAndSend(topic, payload); // WebSocket으로 전송
    }
}
