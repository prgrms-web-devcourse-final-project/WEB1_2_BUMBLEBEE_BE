package roomit.main.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import roomit.main.domain.notification.entity.Notification;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseNotificationDto {
    private Long id;
    private String content;
    private String url;
    private Boolean isRead;
    private LocalDateTime createdAt;

    @Builder
    public ResponseNotificationDto(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.isRead = notification.getIsRead();
        this.createdAt = LocalDateTime.now();
    }
}
