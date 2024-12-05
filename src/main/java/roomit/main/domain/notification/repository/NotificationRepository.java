package roomit.main.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
