package roomit.main.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.ReviewNotification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReviewNotificationRepository extends JpaRepository<ReviewNotification, Long> {
    @Query("SELECT n FROM ReviewNotification n JOIN FETCH n.business b WHERE b.businessId = :businessId ORDER BY n.createdAt DESC")
    List<ReviewNotification> findNotificationsByBusinessId(@Param("businessId") Long businessId);

    @Query("DELETE FROM ReviewNotification r WHERE r.createdAt < :cubeforeDate")
    void deleteByCreatedAtBefore(@Param("cubeforeDate") LocalDateTime cubeforeDate);
}
