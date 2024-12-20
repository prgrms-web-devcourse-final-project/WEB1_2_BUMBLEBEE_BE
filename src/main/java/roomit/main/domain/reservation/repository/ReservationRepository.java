package roomit.main.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member WHERE r.reservationId = :id")
    Optional<Reservation> findByIdWithMember(@Param("id") Long id);

    Reservation findTopByMemberMemberIdOrderByCreatedAtDesc(Long memberId);

    // 내 최근순으로 예약 리스트 출력
    @Query("SELECT r FROM Reservation r WHERE r.member.memberId = :memberId ORDER BY  r.createdAt DESC")
    List<Reservation> findReservationsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    // 내 작업장의 예약 리스트 출력 // Reservation createdAt 인덱싱 추가, Business businessId 인덱싱 추가
    @Query("SELECT r FROM Reservation r JOIN r.studyRoom sr JOIN sr.workPlace wp JOIN wp.business m WHERE m.businessId = :businessId ORDER BY r.createdAt DESC")
    List<Reservation> findMyAllReservations(@Param("businessId") Long businessId);

    // 예약 ID와 회원 ID로 가장 최근 예약 하나 조회
    @Query("SELECT r FROM Reservation r WHERE r.reservationId = :reservationId AND r.member.memberId = :memberId")
    Optional<Reservation> findFirstByIdAndMemberId(@Param("reservationId") Long reservationId, @Param("memberId") Long memberId);

    // 예약 가능한 스터디룸 조회를 위한 지금까지 예약된 내역 조회
    @Query("SELECT r FROM Reservation r WHERE r.studyRoom.studyRoomId = :studyRoomId AND FUNCTION('DATE', r.startTime) = :date")
    List<Reservation> findReservationsByStudyRoomAndDate(@Param("studyRoomId") Long studyRoomId, @Param("date") LocalDate date);

    List<Reservation> findByReservationState(ReservationState reservationState);
}

