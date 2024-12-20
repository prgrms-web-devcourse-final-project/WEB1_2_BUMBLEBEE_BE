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
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<Long, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public void save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId,sseEmitter);
    }

    @Override
    public SseEmitter get(String businessId) {
        return emitters.get(businessId);
    }

    public void deleteById(String userId) {
        emitters.remove(userId);
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
