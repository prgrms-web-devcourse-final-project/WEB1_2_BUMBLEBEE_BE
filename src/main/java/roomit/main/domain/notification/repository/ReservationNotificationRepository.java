package roomit.main.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.ReservationNotification;

import java.util.List;

public interface ReservationNotificationRepository extends JpaRepository<ReservationNotification, Long> {
    @Query("SELECT n FROM ReservationNotification n JOIN FETCH n.business b WHERE b.businessId = :businessId ORDER BY n.createdAt DESC")
    List<ReservationNotification> findNotificationsByBusinessId(@Param("businessId") Long businessId);

}
