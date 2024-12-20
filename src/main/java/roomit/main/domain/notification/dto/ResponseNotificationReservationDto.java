package roomit.main.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.global.inner.ImageUrl;
import roomit.main.global.service.FileLocationService;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationReservationDto {
    private Long reservationId;
    private String content;
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;
    private Long price;
    private String reservationName;
    private String studyRoomName;
    private String workplaceName;
    private String url;

    @Builder
    public ResponseNotificationReservationDto(Notification notification, FileLocationService fileLocationService) {
        this.reservationId = notification.getId();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.notificationType = notification.getNotificationType();
        this.price = notification.getPrice();
        this.reservationName = notification.getReservationName();
        this.studyRoomName = notification.getStudyRoomName();
        this.workplaceName = notification.getWorkplaceName();
        this.url = fileLocationService.getImagesFromFolder(notification.getUrl()).get(0);
    }

    public static ResponseNotificationReservationDto fromEntityReservation(Notification notification, FileLocationService fileLocationService) {
        return new ResponseNotificationReservationDto(
                notification,
                fileLocationService
        );
    }
}
