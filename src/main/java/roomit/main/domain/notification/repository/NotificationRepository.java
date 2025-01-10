package roomit.main.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n JOIN FETCH n.business b WHERE b.businessId = :businessId ORDER BY n.createdAt DESC")
    List<Notification> findNotificationsByBusinessId(@Param("businessId") Long businessId);

    @Query("DELETE FROM Notification r WHERE r.createdAt < :cubeforeDate")
    void deleteByCreatedAtBefore(@Param("cubeforeDate") LocalDateTime cubeforeDate);

}
