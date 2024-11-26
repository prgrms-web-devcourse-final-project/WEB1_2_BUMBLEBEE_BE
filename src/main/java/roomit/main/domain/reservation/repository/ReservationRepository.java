package roomit.main.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    // 내 최근 예약 하나 가져오기
    @Query("SELECT r FROM Reservation r WHERE r.memberId =: memberId ORDER BY  r.createdAt DESC LIMIT 1")
    Optional<ReservationResponse> findRecentReservationByMemberId(@Param("memberId") Long memberId);

    // 내 최근순으로 예약 리스트 출력
    @Query("SELECT r FROM Reservation r WHERE r.memberId =: memberId ORDER BY  r.createdAt DESC")
    List<ReservationResponse> findReservationsByMemberId(Long memberId);

    // 내 작업장의 예약 리스트 출력
    @Query("SELECT r FROM Reservation r JOIN r.studyRoomId sr WHERE sr.workPlaceId = :workPlaceId ORDER BY r.createdAt DESC")
    List<MyWorkPlaceReservationResponse> findMyWorkPlaceReservationsByWorkPlaceId(@Param("workPlaceId") Long workPlaceId);
}
