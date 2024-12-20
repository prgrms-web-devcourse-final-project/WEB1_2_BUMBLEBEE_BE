package roomit.main.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Repository
public interface EmitterRepository {
    void save(String emitterId, SseEmitter sseEmitter);
    SseEmitter get(String businessId);

    void deleteById(String emitterId);
    void deleteAllEventCache();

    void saveEventCache(Long businessId, Object eventData);
    Object getEventCache(Long businessId);
}