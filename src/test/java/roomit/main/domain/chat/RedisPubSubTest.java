package roomit.main.domain.chat;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import roomit.main.domain.chat.redis.service.RedisPublisher;

@SpringBootTest
@ActiveProfiles("test")
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

