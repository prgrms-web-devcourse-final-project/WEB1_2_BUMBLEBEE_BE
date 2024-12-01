package roomit.main.domain.chat.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisLuaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<List> getKeysAndValuesScript;
    private final RedisScript<Long> deleteKeysScript;

    // Lua 스크립트를 사용하여 키와 값을 검색
    public List<Object> getKeysAndValues(String pattern) {
        return redisTemplate.execute(getKeysAndValuesScript, null, pattern);
    }

    // Lua 스크립트를 사용하여 키 삭제
    public Long deleteKeys(String pattern) {
        return redisTemplate.execute(deleteKeysScript, null, pattern);
    }
}
