package roomit.main.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;

    // 예약 만드는 메서드
    @Transactional
    public Long createReservation(Long memberId,Long studyRoomId,CreateReservationRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        if(!validateReservation(request.startTime(),request.endTime())){
            throw new IllegalArgumentException("종료 시간이 시작 시간보다 빠를순 없습니다.");
        }

        Reservation reservation = request.toEntity(member,studyRoom);

        return reservationRepository.save(reservation).getId();
    }

    // validation startTime & endTime(startTime < endTime = True)22
    @Transactional(readOnly = true)
    public boolean validateReservation(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(endTime);

    }

    // x를 눌러 예약을 삭제하는 메서드
    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);
        reservationRepository.delete(reservation);
    }

    // 예약을 수정하는 메서드
    @Transactional
    public void updateReservation(Long reservationId, UpdateReservationRequest request) {
        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        request.updateReservation(existingReservation);
    }

    // memberId를 이용하여 나의 최근 예약 조회
    @Transactional(readOnly = true)
    public ReservationResponse findByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findRecentReservationByMemberId(memberId);

        if (reservations.isEmpty())
        {
            throw new IllegalArgumentException("예약 이력이 없습니다.");
        }
        Reservation recentReservation =  reservations.get(0);
        StudyRoom studyRoom = recentReservation.getStudyRoomId();
        Workplace workplace = studyRoom.getWorkPlace();

        return ReservationResponse.from(studyRoom,recentReservation,workplace);
    }

    // memberId를 이용하여 나의 예약 전체 조회
    @Transactional(readOnly = true)
    public List<ReservationResponse> findReservationsByMemberId(Long memberId){
        List<Reservation> reservations = reservationRepository.findReservationsByMemberId(memberId);

        if(reservations.isEmpty()){
            throw new IllegalArgumentException("예약 이력이 없습니다.");
        }

        return reservations.stream()
                .map(reservation -> ReservationResponse.from(
                        reservation.getStudyRoomId(),
                        reservation,
                        reservation.getStudyRoomId().getWorkPlace()
                ))
                .toList();
    }



    // 내 사업장의 예약자 보기 (예약자 확인 페이지)
    @Transactional(readOnly = true)
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(Long workplaceId) {
        List<Reservation> reservations = reservationRepository.findMyWorkPlaceReservationsByWorkPlaceId(workplaceId);

        return reservations.stream()
                .map(reservation -> MyWorkPlaceReservationResponse.from(
                        reservation.getStudyRoomId(),
                        reservation,
                        reservation.getStudyRoomId().getWorkPlace()
                ))
                .toList();
    }


}
