package roomit.main.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    // 내 최근 예약 하나 가져오기
    @Query("SELECT r FROM Reservation r WHERE r.memberId =: memberId ORDER BY  r.createdAt DESC")
    List<Reservation> findRecentReservationByMemberId(@Param("memberId") Long memberId);

    // 내 최근순으로 예약 리스트 출력
    @Query("SELECT r FROM Reservation r WHERE r.memberId =: memberId ORDER BY  r.createdAt DESC")
    List<Reservation> findReservationsByMemberId(@Param("memberId") Long memberId);

    // 내 작업장의 예약 리스트 출력
    @Query("SELECT r FROM Reservation r JOIN r.studyRoomId sr WHERE sr.workPlaceId.workplaceId = :workplaceId ORDER BY r.createdAt DESC")
    List<Reservation> findMyWorkPlaceReservationsByWorkPlaceId(@Param("workPlaceId") Long workPlaceId);
  
     // 예약 ID와 회원 ID로 가장 최근 예약 하나 조회
     @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.memberId.id = :memberId")
     Optional<Reservation> findFirstByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);



}
