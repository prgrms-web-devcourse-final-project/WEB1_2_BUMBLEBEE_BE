package roomit.main.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.notification.entity.MemberNotification;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationMemberType;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.global.inner.ImageUrl;
import roomit.main.global.service.FileLocationService;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationReservationMemberDto {
    private Long memberalrimId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationMemberType notificationType;
    private Long price;
    private String workplaceName;
    private String studyRoomName;
    private String imageUrl;

    @Builder
    public ResponseNotificationReservationMemberDto(MemberNotification notification, FileLocationService fileLocationService) {
        this.memberalrimId = notification.getMemberNotificatinId();
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.notificationType = notification.getNotificationType();
        this.price = notification.getPrice();
        this.workplaceName = notification.getWorkplaceName();
        this.studyRoomName = notification.getStudyRoomName();
        this.imageUrl = fileLocationService.getImagesFromFolder(notification.getImageUrl()).get(0);
    }

    public static ResponseNotificationReservationMemberDto fromEntityReservationtoMember(MemberNotification memberNotification, FileLocationService fileLocationService) {
        return new ResponseNotificationReservationMemberDto(
                memberNotification,
                fileLocationService
        );
    }
}
