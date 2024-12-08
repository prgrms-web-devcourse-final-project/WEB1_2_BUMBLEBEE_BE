package roomit.main.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.global.inner.ImageUrl;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationReservationDto {
    private Long alrimId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;
    private Long price;
    private String reservationName;
    private String studyRoomName;
    private String workplaceName;
    private String url;

    @Builder
    public ResponseNotificationReservationDto(Notification notification) {
        this.alrimId = notification.getId();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.notificationType = notification.getNotificationType();
        this.price = notification.getPrice();
        this.reservationName = notification.getReservationName();
        this.studyRoomName = notification.getStudyRoomName();
        this.workplaceName = notification.getWorkplaceName();
        this.url = notification.getUrl();
    }

    public static ResponseNotificationReservationDto fromEntityReservation(Notification notification) {
        return new ResponseNotificationReservationDto(
                notification
        );
    }
}
