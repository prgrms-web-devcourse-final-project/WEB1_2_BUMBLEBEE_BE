package roomit.main.domain.notification.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.notification.entity.value.NotificationContent;
import roomit.main.domain.notification.entity.value.RelatedUrl;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.inner.ImageUrl;
import roomit.main.global.service.FileLocationService;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Notification {
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

    private Long workplaceId;

    private String reservationName;

    private String studyRoomName;

    private String workplaceName;

    private String url;

    @Builder
    public Notification(Business business, String url, NotificationType notificationType, String content, Long price, Long workplaceId, String reservationName, String studyRoomName, String workplaceName) {
        this.business = business;
        this.notificationType = NotificationType.valueOf(notificationType.name());
        this.content = new NotificationContent(content);
        this.createdAt = LocalDateTime.now();
        this.price = price;
        this.workplaceId = workplaceId;
        this.reservationName = reservationName;
        this.studyRoomName = studyRoomName;
        this.workplaceName = workplaceName;
        this.url = url;
    }

    public static Notification toentity(Business business, Workplace workplace, Reservation reservation, String content, Long price){
        Notification notification = Notification.builder()
                .business(business)
                .workplaceId(workplace.getWorkplaceId())
                .notificationType(NotificationType.RESERVATION_CONFIRMED)
                .content(content)
                .price(price)
                .url(workplace.getImageUrl().getValue())
                .reservationName(reservation.getReservationName().getValue())
                .studyRoomName(reservation.getStudyRoom().getStudyRoomName().getValue())
                .workplaceName(workplace.getWorkplaceName().getValue())
                .build();

        return notification;
    }


    public String getContent(){
        return content.getContent();
    }
}