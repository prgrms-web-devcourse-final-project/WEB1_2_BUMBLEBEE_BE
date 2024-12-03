package roomit.main.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.domain.notification.repository.EmitterRepository;
import roomit.main.domain.notification.repository.NotificationRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static software.amazon.awssdk.services.s3.endpoints.internal.ParseArn.SERVICE;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final BusinessRepository businessRepository;

    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분으로 변경


    public SseEmitter subscribe(Long businessId, String lastEventId){


        String emitterId = businessId + "_" + System.currentTimeMillis();

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        Map<String, Object> testContent = new HashMap<>();
        testContent.put("content", "connected!");
        sendToClient(emitter, businessId, testContent);

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); //완료 시, 타임아웃 시, 에러 발생 시
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e) -> emitterRepository.deleteById(emitterId));

//        sendToClient(emitter, Long.valueOf(emitterId), "EventStream Created. [memberId=" + businessId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(businessId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, Long.valueOf(entry.getKey()), entry.getValue()));
        }

        return emitter;
    }

    private synchronized void sendToClient(SseEmitter emitter, Long emitterId, Object data) throws RuntimeException {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(emitterId))
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(String.valueOf(emitterId));
            throw new IllegalArgumentException();
        }
    }

    public void notify(Long businessId, ResponseNotificationDto notificationDTO) {
        SseEmitter emitter = emitterRepository.get(String.valueOf(businessId));
        if (emitter != null) {
            sendToClient(emitter, businessId, notificationDTO);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
