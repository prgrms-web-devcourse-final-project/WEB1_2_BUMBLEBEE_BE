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
import roomit.main.global.service.FileLocationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberNotificationService {

    private final MemberNotificationRepository memberNotificationRepository;

    private final FileLocationService fileLocationService;

    @Transactional
    public List<ResponseNotificationReservationMemberDto> getNotificationsReservationMember(Long businessId) {

        List<MemberNotification> notifications = memberNotificationRepository.findNotificationsByBusinessId(businessId);

        return notifications.stream()
                .map(memberNotification -> ResponseNotificationReservationMemberDto.fromEntityReservationtoMember(memberNotification, fileLocationService))  // Notification -> NotificationDto 변환
                .toList();
    }

    // 3개월이 지난 회원 예약 알림 제거
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteOldReservationNotifications() {
        memberNotificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusMonths(3));
    }
}
