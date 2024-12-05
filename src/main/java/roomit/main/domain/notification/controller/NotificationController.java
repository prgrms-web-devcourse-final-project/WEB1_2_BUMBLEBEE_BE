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
import roomit.main.domain.notification.service.NotificationService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal CustomBusinessDetails customBusinessDetails) {
        Long businessId = customBusinessDetails.getId();
        return ResponseEntity.ok(notificationService.subscribe(businessId));
    }
}
