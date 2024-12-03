package roomit.main.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    SseEmitter get(String businessId);
    void saveEventCache(String emitterId, Object event);

    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    Map<String,Object> findAllEventCacheStartWithByMemberId(String memberId);

    void deleteById(String emitterId);
    void  deleteAllEmitterStartWithId(String memberId);
    void deleteAllEventCacheStartWithId(String memberId);

}