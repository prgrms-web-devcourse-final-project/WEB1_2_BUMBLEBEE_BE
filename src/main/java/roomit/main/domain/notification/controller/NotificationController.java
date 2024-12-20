package roomit.main.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationDto;
import roomit.main.domain.notification.dto.ResponseNotificationReservationMemberDto;
import roomit.main.domain.notification.service.MemberNotificationService;
import roomit.main.domain.notification.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    private final MemberNotificationService memberNotificationService;

    // 사업자 구독
    @GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        Long businessId = customBusinessDetails.getId();
        String emitterKey = "business-" + businessId;
        return ResponseEntity.ok(notificationService.subscribe(emitterKey));
    }

    // 멤버 구독
    @GetMapping(value = "/api/subscribe/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeUser(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Long memberId = customMemberDetails.getId();  // 사용자 ID
        String emitterKey = "member-" + memberId;
        return ResponseEntity.ok(notificationService.subscribe(emitterKey));
    }

    //리뷰
    @GetMapping("/api/v1/sub/list")
    public ResponseEntity<List<ResponseNotificationDto>> read(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        Long businessId = customBusinessDetails.getId();
        return ResponseEntity.ok(notificationService.getNotifications(businessId));
    }

    //예약
    @GetMapping("/api/v1/subReservation/list")
    public ResponseEntity<List<ResponseNotificationReservationDto>> reads(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        Long businessId = customBusinessDetails.getId();
        return ResponseEntity.ok(notificationService.getNotificationsReservation(businessId));
    }

    @GetMapping("/api/v1/subReservation/memberlist")
    public ResponseEntity<List<ResponseNotificationReservationMemberDto>> readsMember(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Long memberDetailsId = customMemberDetails.getId();
        return ResponseEntity.ok(memberNotificationService.getNotificationsReservationMember(memberDetailsId));
    }
}
