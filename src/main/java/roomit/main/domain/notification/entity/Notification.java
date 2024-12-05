package roomit.main.domain.notification.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.value.NotificationContent;
import roomit.main.domain.notification.entity.value.RelatedUrl;
import roomit.main.domain.workplace.entity.Workplace;

@Getter
@Entity
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationContent content;
    @Embedded
    private RelatedUrl url;

    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    @Builder
    public Notification(Business business, NotificationType notificationType, String content, String url) {
        this.business = business;
        this.notificationType = NotificationType.REVIEW_CREATED;
        this.content = new NotificationContent(content);
        this.url = new RelatedUrl(url);
        this.isRead = false;
    }
    public void read(){
        isRead = true;
    }

    public String getContent(){
        return content.getContent();
    }
    public String getUrl(){
        return url.getUrl();
    }
}