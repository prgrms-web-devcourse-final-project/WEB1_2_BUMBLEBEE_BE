package roomit.main.domain.chat.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.chat.redis.service.RedisLuaService;

import java.util.List;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisLuaService redisLuaService;

    @GetMapping("/keys-and-values")
    public List<Object> getKeysAndValues(@RequestParam String pattern) {
        return redisLuaService.getKeysAndValues(pattern);
    }

    @DeleteMapping("/keys")
    public Long deleteKeys(@RequestParam String pattern) {
        return redisLuaService.deleteKeys(pattern);
    }
}
