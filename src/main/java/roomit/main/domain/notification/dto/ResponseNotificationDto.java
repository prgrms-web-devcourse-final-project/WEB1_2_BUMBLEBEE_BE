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
import roomit.main.global.service.FileLocationService;

@Getter
@Setter
@ToString
public class ResponseNotificationDto {
    private Long reviewId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationType notificationType;
    private String workplaceName;
    private String imageURL;

    @Builder
    public ResponseNotificationDto(ReviewNotification notification, FileLocationService fileLocationService) {
        this.reviewId = notification.getId();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.workplaceName = notification.getWorkplaceName();
        this.notificationType = notification.getReveiewNotificationType();
        this.imageURL = fileLocationService.getImagesFromFolder(notification.getUrl()).get(0);
    }

    public static ResponseNotificationDto fromEntity(ReviewNotification notification, FileLocationService fileLocationService) {
        return new ResponseNotificationDto(
                notification,
            fileLocationService
        );
    }
}
