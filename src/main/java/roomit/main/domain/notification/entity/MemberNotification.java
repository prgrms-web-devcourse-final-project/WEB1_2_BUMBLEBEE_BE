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

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class MemberNotification {
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;

    private Long price;

    private Long workplaceId;

    @Builder
    public MemberNotification(Member member, NotificationType notificationType, String content, Long price, Long workplaceId) {
        this.member = member;
        this.notificationType = NotificationType.valueOf(notificationType.name());
        this.content = new NotificationContent(content);
        this.createdAt = LocalDateTime.now();
        this.price = price;
        this.workplaceId = workplaceId;
    }

    public String getContent(){
        return content.getContent();
    }
}