package roomit.main.domain.chat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.junit4.SpringRunner;
import roomit.main.domain.chat.service.RedisPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class RedisPubSubTest {

    @MockBean
    private RedisPublisher redisPublisher;

    @Test
    void testRedisPublish() {
        // RedisPublisher Mock 동작 설정
        Mockito.doNothing().when(redisPublisher).publish(any(String.class), any(Object.class));

        // 테스트 실행
        redisPublisher.publish("/sub/chat/room/1", "Test message");

        // verify 사용해 호출 확인
        Mockito.verify(redisPublisher, Mockito.times(1)).publish("/sub/chat/room/1", "Test message");
    }
}

