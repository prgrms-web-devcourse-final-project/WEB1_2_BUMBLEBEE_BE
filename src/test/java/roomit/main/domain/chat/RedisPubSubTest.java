package roomit.main.domain.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.junit4.SpringRunner;
import roomit.main.domain.chat.service.RedisPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisPubSubTest {

    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    private RedisMessageListenerContainer listenerContainer;

    @Test
    void testRedisPubSub() throws InterruptedException {
        String testChannel = "/sub/chat/room/1";
        String testMessage = "Test message";

        // 구독 등록
        listenerContainer.addMessageListener((message, pattern) -> {
            String topic = new String(pattern);
            String payload = new String(message.getBody());
            assertEquals(testChannel, topic); // 주제가 일치하는지 확인
            assertEquals(testMessage, payload); // 메시지가 일치하는지 확인
            System.out.println("Received message: " + payload);
        }, new ChannelTopic(testChannel));

        // 메시지 발행
        redisPublisher.publish(testChannel, testMessage);

        // 메시지 처리를 위한 대기
        Thread.sleep(1000);
    }
}
