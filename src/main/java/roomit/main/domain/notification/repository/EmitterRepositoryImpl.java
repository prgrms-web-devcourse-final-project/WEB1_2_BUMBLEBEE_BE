package roomit.main.domain.notification.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public void save(Long emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId,sseEmitter);
    }

    @Override
    public SseEmitter get(Long businessId) {
        return emitters.get(businessId);
    }

    public void deleteById(Long userId) {
        emitters.remove(userId);
    }

    @Override
    public Map<Long, SseEmitter> getAll() {
        return emitters;
    }

    @Override
    public void deleteAllEventCache() {
        eventCache.clear();
    }

    public void saveEventCache(Long businessId, Object eventData) {
        eventCache.put(businessId, eventData);
    }

    public Object getEventCache(Long businessId) {
        return eventCache.get(businessId);
    }


}
