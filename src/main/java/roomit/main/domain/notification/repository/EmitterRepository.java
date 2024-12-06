package roomit.main.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Repository
public interface EmitterRepository {
    void save(Long emitterId, SseEmitter sseEmitter);
    SseEmitter get(Long businessId);

    void deleteById(Long emitterId);
    Map<Long, SseEmitter> getAll();
    void deleteAllEventCache();

    void saveEventCache(Long businessId, Object eventData);
    Object getEventCache(Long businessId);
}