package roomit.main.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationDto {
    private String content;
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;

    @Builder
    public ResponseNotificationDto(Notification notification, Long workplaceId) {
        this.content = notification.getContent();
        this.createdAt = LocalDateTime.now();
        this.workplaceId = workplaceId;
        this.notificationType = notification.getNotificationType();
    }
}
