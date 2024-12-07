package roomit.main.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationReservationDto {
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;
    private Long price;

    @Builder
    public ResponseNotificationReservationDto(Notification notification, Long workplaceId) {
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = workplaceId;
        this.notificationType = notification.getNotificationType();
        this.price = notification.getPrice();
    }

    public static ResponseNotificationReservationDto fromEntityReservation(Notification notification, Long workplaceId) {
        return new ResponseNotificationReservationDto(
                notification,
                workplaceId
        );
    }
}
