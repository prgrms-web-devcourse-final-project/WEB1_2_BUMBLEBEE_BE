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

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationReservationMemberDto {
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Long workplaceId;
    private NotificationMemberType notificationType;
    private Long price;

    @Builder
    public ResponseNotificationReservationMemberDto(MemberNotification notification) {
        this.content = notification.getContent();
        this.createdAt = notification.getCreatedAt();
        this.workplaceId = notification.getWorkplaceId();
        this.notificationType = notification.getNotificationType();
        this.price = notification.getPrice();
    }

    public static ResponseNotificationReservationMemberDto fromEntityReservationtoMember(MemberNotification memberNotification) {
        return new ResponseNotificationReservationMemberDto(
                memberNotification
        );
    }
}
