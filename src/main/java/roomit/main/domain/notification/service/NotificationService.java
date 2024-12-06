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
import roomit.main.domain.notification.dto.ResponseNotificationReservationDto;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.domain.notification.repository.EmitterRepository;
import roomit.main.domain.notification.repository.NotificationRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static software.amazon.awssdk.services.s3.endpoints.internal.ParseArn.SERVICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final BusinessRepository businessRepository;

    private final WorkplaceRepository workplaceRepository;

    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분으로 변경


    public SseEmitter subscribe(Long businessId) {


        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(businessId, emitter);

        Object cachedEvent = emitterRepository.getEventCache(businessId);
        if (cachedEvent != null) {
            sendToClient(emitter, businessId, cachedEvent);
        } else {
            Map<String, Object> testContent = new HashMap<>();
            testContent.put("content", "connected!");
            sendToClient(emitter, businessId, testContent);
        }

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

    // 리뷰
    public  void customNotify(Long businessId, ResponseNotificationDto responseNotificationDto) {

        Business business = businessRepository.findById(businessId).get();

        Notification notification = Notification.builder()
                .business(business)
                .content(responseNotificationDto.getContent())
                .notificationType(responseNotificationDto.getNotificationType())
                .build();

        notificationRepository.save(notification);

        cacheEvent(businessId, responseNotificationDto);
        SseEmitter sseEmitter = emitterRepository.get(businessId);
        if (sseEmitter != null) {
            sendToClient(businessId, responseNotificationDto);
        }

    }
    // 예약
    public  void customNotifyReservation(Long businessId, ResponseNotificationReservationDto responseNotificationReservationDto) {

        Business business = businessRepository.findById(businessId).get();

        Notification notification = Notification.builder()
                .business(business)
                .content(responseNotificationReservationDto.getContent())
                .notificationType(responseNotificationReservationDto.getNotificationType())
                .build();

        notification.setPrice(responseNotificationReservationDto.getPrice());

        notificationRepository.save(notification);

        cacheEvent(businessId, responseNotificationReservationDto);
        SseEmitter sseEmitter = emitterRepository.get(businessId);
        if (sseEmitter != null) {
            sendToClient(businessId, responseNotificationReservationDto);
        }

    }

    /**
     * 이벤트 캐싱 저장
     */
    private void cacheEvent(Long businessId, Object data) {
        emitterRepository.saveEventCache(businessId, data);
    }

    // 맨처음 구독시 오는곳
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

    private <T> void sendToClient(Long userId, T data) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .data(data));
                log.info("sendToClient emitterId={} data={} ", userId, data);
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    @Scheduled(fixedRate = 30000)// 30초 간격
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

    @Transactional
    public List<ResponseNotificationDto> getNotifications(Long businessId, Long workplaceID) {


        List<Notification> notifications = notificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map(notification -> ResponseNotificationDto.fromEntity(notification, workplaceID))  // Notification -> NotificationDto 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ResponseNotificationReservationDto> getNotificationsReservation(Long businessId, Long workplaceID, Long price) {


        List<Notification> notifications = notificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map(notification -> ResponseNotificationReservationDto.fromEntityReservation(notification, workplaceID, price))  // Notification -> NotificationDto 변환
                .collect(Collectors.toList());
    }
}
