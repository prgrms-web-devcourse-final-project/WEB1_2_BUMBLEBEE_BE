package roomit.main.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
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
import roomit.main.global.error.ErrorCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static software.amazon.awssdk.services.s3.endpoints.internal.ParseArn.SERVICE;
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final BusinessRepository businessRepository;

    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분으로 변경


    public SseEmitter subscribe(Long businessId){


        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(businessId, emitter);

        Map<String, Object> testContent = new HashMap<>();
        testContent.put("content", "connected!");
        sendToClient(emitter, businessId, testContent);
        emitter.onError((ex) -> {
            log.error("SSE connection error for businessId={}: {}", businessId, ex.getMessage());
            emitterRepository.deleteById(businessId);
        });

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for businessId={}", businessId);
            emitterRepository.deleteById(businessId);
        });

        emitter.onTimeout(() -> {
            log.warn("SSE connection timeout for businessId={}", businessId);
            emitter.complete();
            emitterRepository.deleteById(businessId);
        });

//        sendToClient(emitter, Long.valueOf(emitterId), "EventStream Created. [memberId=" + businessId + "]");


        return emitter;
    }

    private void sendToClient(SseEmitter emitter, Long emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(emitterId))
                    .data(data, MediaType.APPLICATION_JSON));
            log.info("sendToClient emitterId={} data={}", emitterId, data);
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            emitter.completeWithError(exception);
            throw ErrorCode.SUBSCRIBE_FAIL.commonException();
        }
    }

    public void notify(Long businessId, ResponseNotificationDto notificationDTO) {
        SseEmitter emitter = emitterRepository.get(businessId);
        if (emitter != null) {
            sendToClient(emitter, businessId, notificationDTO);
        } else {
            log.warn("No active SSE connection for memberId: {}", businessId);
        }
    }

    @Scheduled(fixedRate = 60000) // 1분 간격
    public void cleanUpExpiredEmitters() {
        emitterRepository.getAll().forEach((businessId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().comment("Heartbeat")); // 연결 확인
            } catch (Exception e) {
                emitterRepository.deleteById(businessId); // 연결 실패 시 제거
                emitterRepository.deleteAllEventCache(); // 관련 캐시 제거
            }
        });
    }

}
