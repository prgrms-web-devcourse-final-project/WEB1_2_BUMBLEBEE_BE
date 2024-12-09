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
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationMemberDto;
import roomit.main.domain.notification.entity.MemberNotification;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.ReviewNotification;
import roomit.main.domain.notification.repository.EmitterRepository;
import roomit.main.domain.notification.repository.MemberNotificationRepository;
import roomit.main.domain.notification.repository.NotificationRepository;
import roomit.main.domain.notification.repository.ReviewNotificationRepository;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.service.FileLocationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final ReviewNotificationRepository reviewNotificationRepository;

    private final BusinessRepository businessRepository;

    private final MemberRepository memberRepository;

    private final MemberNotificationRepository memberNotificationRepository;

    private final FileLocationService fileLocationService;

    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분으로 변경


    public SseEmitter subscribe(String id) {


        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(id, emitter);

            Map<String, Object> testContent = new HashMap<>();
            testContent.put("content", "connected!");
            sendToClient(emitter, id, testContent);

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
    // 맨처음 구독시 오는곳
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data, MediaType.APPLICATION_JSON));
            log.info("sendToClient emitterId={} data={}", emitterId, data);
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            emitter.completeWithError(exception);
            throw ErrorCode.SUBSCRIBE_FAIL.commonException();
        }
    }

    private <T> void sendToClient(String userId, T data) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(userId)
                        .data(data));
                log.info("sendToClient emitterId={} data={} ", userId, data);
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }
    // 리뷰
    public void customNotify(String businessId, ResponseNotificationDto responseNotificationDto) {

        Business business = businessRepository.findById(Long.valueOf(businessId)).get();

        ReviewNotification notification = ReviewNotification.builder()
                .business(business)
                .workplaceName(responseNotificationDto.getWorkplaceName())
                .content(responseNotificationDto.getContent())
                .notificationType(responseNotificationDto.getNotificationType())
                .workplaceId(responseNotificationDto.getWorkplaceId())
                .url(responseNotificationDto.getImageURL())
                .build();

        reviewNotificationRepository.save(notification);
        String emitterKey = "business-" + businessId;
        SseEmitter sseEmitter = emitterRepository.get(emitterKey);
        if (sseEmitter != null) {
            sendToClient(emitterKey, responseNotificationDto);
        }
    }

    // 사업자 예약
    public void customNotifyReservation(String businessId, ResponseNotificationReservationDto responseNotificationReservationDto) {

        Business business = businessRepository.findById(Long.valueOf(businessId)).get();

        Notification reservationNotification = Notification.builder()
                .business(business)
                .price(responseNotificationReservationDto.getPrice())
                .workplaceId(responseNotificationReservationDto.getWorkplaceId())
                .content(responseNotificationReservationDto.getContent())
                .notificationType(responseNotificationReservationDto.getNotificationType())
                .workplaceName(responseNotificationReservationDto.getWorkplaceName())
                .reservationName(responseNotificationReservationDto.getReservationName())
                .studyRoomName(responseNotificationReservationDto.getStudyRoomName())
                .url((responseNotificationReservationDto.getUrl()))
                .build();

        notificationRepository.save(reservationNotification);
        String emitterKey = "business-" + businessId;
        SseEmitter sseEmitter = emitterRepository.get(emitterKey);
        if (sseEmitter != null) {
            sendToClient(emitterKey, responseNotificationReservationDto);
        }
    }

    public void customNotifyReservationMember(String memberId, ResponseNotificationReservationMemberDto responseNotificationReservationDto, Long price) {

        Member member = memberRepository.findById(Long.valueOf(memberId)).get();

        MemberNotification memberNotification = MemberNotification.builder()
                .member(member)
                .workplaceId(responseNotificationReservationDto.getWorkplaceId())
                .content(responseNotificationReservationDto.getContent())
                .notificationType(responseNotificationReservationDto.getNotificationType())
                .imageUrl((responseNotificationReservationDto.getImageUrl()))
                .studyRoomName(responseNotificationReservationDto.getStudyRoomName())
                .workplaceName(responseNotificationReservationDto.getWorkplaceName())
                .price(price)
                .build();

        memberNotificationRepository.save(memberNotification);
        String emitterKey = "member-" + memberId;
        SseEmitter sseEmitter = emitterRepository.get(emitterKey);
        if (sseEmitter != null) {
            sendToClient(emitterKey, responseNotificationReservationDto);
        }
    }
    


    @Transactional
    public List<ResponseNotificationDto> getNotifications(Long businessId) {


        List<ReviewNotification> notifications = reviewNotificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map((ReviewNotification notification) -> ResponseNotificationDto.fromEntity(
                    notification, fileLocationService))  // Notification -> NotificationDto 변환
                .toList();
    }

    @Transactional
    public List<ResponseNotificationReservationDto> getNotificationsReservation(Long businessId) {

        List<Notification> notifications = notificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map((Notification notification) -> ResponseNotificationReservationDto.fromEntityReservation(notification, fileLocationService))  // Notification -> NotificationDto 변환
                .toList();
    }
}
