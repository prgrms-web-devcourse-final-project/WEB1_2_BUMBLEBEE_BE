package roomit.main.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.domain.notification.entity.ReviewNotification;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationDto {
    private Long alrimId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;

    @Builder
    public ResponseNotificationDto(ReviewNotification notification) {
        this.alrimId = notification.getId();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.notificationType = notification.getNotificationType();
    }

    public static ResponseNotificationDto fromEntity(ReviewNotification notification) {
        return new ResponseNotificationDto(
                notification
        );
    }
}
