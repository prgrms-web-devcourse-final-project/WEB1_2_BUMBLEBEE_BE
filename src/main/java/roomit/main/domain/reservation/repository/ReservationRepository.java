package roomit.main.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.reservation.entity.Reservation;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.member.memberId = :memberId ORDER BY r.reservationPhoneNumber DESC")
    Optional<Reservation> findRecentReservationByMemberId(@Param("memberId") Long memberId);
}
