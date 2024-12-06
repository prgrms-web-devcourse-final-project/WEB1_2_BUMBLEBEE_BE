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
public class ReservationNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationContent content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @CreatedDate
    private LocalDateTime createdAt;

    private Long price;


    @Builder
    public ReservationNotification(Business business, NotificationType notificationType, String content, Long price) {
        this.business = business;
        this.notificationType = NotificationType.valueOf(notificationType.name());
        this.content = new NotificationContent(content);
        this.createdAt = LocalDateTime.now();
        this.price = price;
    }

    public String getContent(){
        return content.getContent();
    }
}