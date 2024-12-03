package roomit.main.domain.notification.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {
    @Column(nullable = false)
    private String content;

    public NotificationContent(String content){
        this.content = content;
    }
}
