package roomit.main.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.notification.entity.MemberNotification;
import roomit.main.domain.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {
    @Query("SELECT n FROM MemberNotification n JOIN FETCH n.member b WHERE b.memberId = :businessId ORDER BY n.createdAt DESC")
    List<MemberNotification> findNotificationsByBusinessId(@Param("businessId") Long businessId);

    @Query("DELETE FROM MemberNotification r WHERE r.createdAt < :cubeforeDate")
    void deleteByCreatedAtBefore(@Param("cubeforeDate") LocalDateTime cubeforeDate);

}
