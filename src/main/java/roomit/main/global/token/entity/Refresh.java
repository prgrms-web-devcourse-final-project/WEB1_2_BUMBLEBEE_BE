package roomit.main.global.token.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 3600)
@Getter
public class Refresh {

    @Id
    private String refresh;

    private String username;

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeRefresh(String refresh) {
        this.refresh = refresh;
    }
}
