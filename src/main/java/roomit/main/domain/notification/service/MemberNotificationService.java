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
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationMemberDto;
import roomit.main.domain.notification.entity.MemberNotification;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.repository.EmitterRepository;
import roomit.main.domain.notification.repository.MemberNotificationRepository;
import roomit.main.domain.notification.repository.NotificationRepository;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberNotificationService {

    private final EmitterRepository emitterRepository;

    private final MemberNotificationRepository memberNotificationRepository;

    private final MemberRepository memberRepository;

    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분으로 변경


    public SseEmitter subscribe(Long id) {


        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(id, emitter);

        Object cachedEvent = emitterRepository.getEventCache(id);
        if (cachedEvent != null) {
            sendToClient(emitter, id, cachedEvent);
        } else {
            Map<String, Object> testContent = new HashMap<>();
            testContent.put("Member subscribe content", "connected!");
            sendToClient(emitter, id, testContent);
        }

        emitter.onError((ex) -> {
            log.error("SSE connection error for businessId={}: {}", id, ex.getMessage());
            emitterRepository.deleteById(id);
        });

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for businessId={}", id);
            emitterRepository.deleteById(id);
        });

        emitter.onTimeout(() -> {
            log.warn("SSE connection timeout for businessId={}", id);
            emitter.complete();
            emitterRepository.deleteById(id);
        });

        return emitter;
    }

    // 멤버 예약 알림
    public void customNotifyReservationMember(Long memberId, ResponseNotificationReservationMemberDto responseNotificationReservationDto, Long price) {

        Member member = memberRepository.findById(memberId).get();

        MemberNotification memberNotification = MemberNotification.builder()
                .member(member)
                .workplaceId(responseNotificationReservationDto.getWorkplaceId())
                .content(responseNotificationReservationDto.getContent())
                .notificationType(responseNotificationReservationDto.getNotificationType())
                .price(price)
                .build();

        memberNotificationRepository.save(memberNotification);

        cacheEvent(memberId, responseNotificationReservationDto);
        SseEmitter sseEmitter = emitterRepository.get(memberId);
        if (sseEmitter != null) {
            sendToClient(memberId, responseNotificationReservationDto);
        }
    }

    private void cacheEvent(Long businessId, Object data) {
        emitterRepository.saveEventCache(businessId, data);
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
    public List<ResponseNotificationReservationMemberDto> getNotificationsReservationMember(Long businessId) {

        List<MemberNotification> notifications = memberNotificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map(ResponseNotificationReservationMemberDto::fromEntityReservationtoMember)  // Notification -> NotificationDto 변환
                .toList();
    }
}
