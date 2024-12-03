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
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;

    // 예약 만드는 메서드
    @Transactional
    public Long createReservation(Long memberId,Long studyRoomId,CreateReservationRequest request) {
        if(!validateReservation(request.startTime(),request.endTime())){
            throw ErrorCode.START_TIME_NOT_AFTER_END_TIME.commonException();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        Reservation reservation = request.toEntity(member,studyRoom);

        return reservationRepository.save(reservation).getReservationId();
    }

    // validation startTime & endTime(startTime < endTime = True)
    @Transactional(readOnly = true)
    public boolean validateReservation(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(endTime);
    }



//    // 중복 예약 방지 로직
//    private boolean isDuplicateReservation(Long studyRoomId,LocalDateTime startTime,LocalDateTime endTime){
//
//    }

    // x를 눌러 예약을 삭제하는 메서드
    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = reservation.getStartTime();

        if(now.isBefore(reservationTime.minusDays(2))){
            // 100프로 환불 메서드
            reservation.changeReservationState(ReservationState.CANCELLED);
            System.out.println("2일 전 취소: 무료 취소 처리되었습니다.");
        }else if(now.isBefore(reservationTime.minusDays(1))){
            // 50프로 환불 메서드
            reservation.changeReservationState(ReservationState.CANCELLED);
            System.out.println("1일 전 취소: 수수료 50% 부과되었습니다.");
        }else{
            throw ErrorCode.RESERVATION_CANNOT_CANCEL.commonException();
        }
    }

    // 예약을 수정하는 메서드
    @Transactional
    public void updateReservation(Long reservationId,Long memberId ,UpdateReservationRequest request) {
        validateReservationOwner(reservationId,memberId);

        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);
        try {
            existingReservation.updateReservationDetails(
                    request.reservationName(),
                    request.reservationPhoneNumber(),
                    request.startTime(),
                    request.endTime()
            );
        }catch (Exception e){
            throw ErrorCode.RESERVATION_NOT_MODIFIED.commonException();
        }
    }

    @Transactional(readOnly = true)
    public void validateReservationOwner(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(ErrorCode.RESERVATION_NOT_FOUND::commonException);

    // 예약을 만든 사람과 수정 요청자가 동일한지 확인
        if (!reservation.getMember().getMemberId().equals(memberId)) {
            throw ErrorCode.RESERVATION_NOT_MODIFIED.commonException();  // 수정 권한이 없을 경우 예외 처리
        }
    }


    // memberId를 이용하여 나의 최근 예약 조회
    @Transactional(readOnly = true)
    public ReservationResponse findByMemberId(Long memberId) {
        List<Reservation> recentReservation = reservationRepository.findRecentReservationByMemberId(memberId);

        if (recentReservation == null)
        {
            throw(ErrorCode.RESERVATION_IS_EMPTY.commonException());
        }

        Reservation recentReservation1 = recentReservation.get(0);
        StudyRoom studyRoom = recentReservation1.getStudyRoom();
        Workplace workplace = studyRoom.getWorkPlace();

        return ReservationResponse.from(studyRoom,recentReservation1,workplace);
    }

    // memberId를 이용하여 나의 예약 전체 조회
    @Transactional(readOnly = true)
    public List<ReservationResponse> findReservationsByMemberId(Long memberId){
        List<Reservation> reservations = reservationRepository.findReservationsByMemberId(memberId);

        if(reservations.isEmpty()){
            throw(ErrorCode.RESERVATION_IS_EMPTY.commonException());
        }

        return reservations.stream()
                .map(reservation -> ReservationResponse.from(
                        reservation.getStudyRoom(),
                        reservation,
                        reservation.getStudyRoom().getWorkPlace()
                ))
                .toList();
    }



    // 내 사업장의 예약자 보기 (예약자 확인 페이지)
    @Transactional(readOnly = true)
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(Long workplaceId) {
        List<Reservation> reservations = reservationRepository.findMyWorkPlaceReservationsByWorkPlaceId(workplaceId);

        if(reservations.isEmpty()){
            throw(ErrorCode.RESERVATION_IS_EMPTY.commonException());
        }

        return reservations.stream()
                .map(reservation -> MyWorkPlaceReservationResponse.from(
                        reservation.getStudyRoom(),
                        reservation,
                        reservation.getStudyRoom().getWorkPlace()
                ))
                .toList();
    }

}
