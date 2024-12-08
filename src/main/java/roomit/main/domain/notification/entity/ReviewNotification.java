package roomit.main.domain.notification.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.value.NotificationContent;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class ReviewNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationContent reveiewNotificationContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType reveiewNotificationType;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @CreatedDate
    private LocalDateTime createdAt;

    private Long workplaceId;

    private String studyRoomName;

    private String workplaceName;

    private String url;
    @Builder
    public ReviewNotification(Business business, String url, NotificationType notificationType, String content, Long workplaceId,String studyRoomName, String workplaceName) {
        this.business = business;
        this.reveiewNotificationType = NotificationType.valueOf(notificationType.name());
        this.reveiewNotificationContent = new NotificationContent(content);
        this.createdAt = LocalDateTime.now();
        this.workplaceId = workplaceId;
        this.studyRoomName = studyRoomName;
        this.workplaceName = workplaceName;
        this.url = url;
    }

    public String getContent(){
        return reveiewNotificationContent.getContent();
    }
}