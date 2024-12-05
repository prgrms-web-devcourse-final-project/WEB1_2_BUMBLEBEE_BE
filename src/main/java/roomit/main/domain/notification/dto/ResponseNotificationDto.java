package roomit.main.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.Notification;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResponseNotificationDto {
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private Long workplaceId;

    @Builder
    public ResponseNotificationDto(Notification notification, Long workplaceId) {
        this.content = notification.getContent();
        this.isRead = notification.getIsRead();
        this.createdAt = LocalDateTime.now();
        this.workplaceId = workplaceId;
    }
}
