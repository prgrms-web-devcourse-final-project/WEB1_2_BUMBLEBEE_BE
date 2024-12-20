package roomit.main.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.rock.DistributedLock;
import roomit.main.global.service.FileLocationService;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;
    private final FileLocationService fileLocationService;

    // 예약 만드는 메서드
    @DistributedLock(key = "#studyRoomId + ':' + #request.startTime + ':' + #request.endTime")
    public Long createReservation(Long memberId,Long studyRoomId,CreateReservationRequest request) {
        validateReservation(request.startTime(),request.endTime());

        checkReservationTime(request.startTime(),request.endTime(),studyRoomId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.BUSINESS_NOT_FOUND::commonException);

        StudyRoom studyRoom = studyRoomRepository.findByIdWithWorkplace(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        Reservation reservation = request.toEntity(member,studyRoom);

        return reservationRepository.save(reservation).getReservationId();
    }

    @Transactional(readOnly = true)
    public void validateReservation(LocalDateTime startTime, LocalDateTime endTime) {
        if(!startTime.isBefore(endTime)){
            throw ErrorCode.START_TIME_NOT_AFTER_END_TIME.commonException();
        }
    }

    // 예약의 중복 시간 체크
    @Transactional(readOnly = true)
    public void checkReservationTime(LocalDateTime StartTime, LocalDateTime endTime, Long studyRoomId) {
        StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);

        existingStudyRoom.getReservations().forEach(reservation -> {
            if (reservation.getReservationState().equals(ReservationState.ACTIVE) || reservation.getReservationState().equals(ReservationState.ON_HOLD)) {
                if (reservation.getStartTime().isBefore(endTime) && reservation.getEndTime().isAfter(StartTime)) {
                    throw ErrorCode.DUPLICATE_RESERVATION.commonException();
                }
            }
        });
    }


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
        }else if(now.isBefore(reservationTime.minusDays(1))){
            // 50프로 환불 메서드
            reservation.changeReservationState(ReservationState.CANCELLED);
        }else{
            throw ErrorCode.RESERVATION_CANNOT_CANCEL.commonException();
        }
    }

    // 제일 최근 예약 1건 조회
    @Transactional(readOnly = true)
    public ReservationResponse findByMemberId(Long memberId) {
        Reservation reservation = reservationRepository.findTopByMemberMemberIdOrderByCreatedAtDesc(memberId);

        if(reservation == null){
            return null;
        }

        StudyRoom studyRoom = reservation.getStudyRoom();
        Workplace workPlace = studyRoom.getWorkPlace();


        return ReservationResponse.from(studyRoom,reservation,workPlace,fileLocationService);
    }



    // memberId를 이용하여 나의 예약 전체 조회
    @Transactional(readOnly = true)
    public List<ReservationResponse> findReservationsByMemberId(Long memberId){
        Pageable pageable = PageRequest.of(0, 10);

        List<Reservation> reservations = reservationRepository.findReservationsByMemberId(memberId, pageable);

        if(reservations.isEmpty()){
            return null;
        }

        return reservations.stream()
            .map(reservation -> ReservationResponse.from(
                reservation.getStudyRoom(),
                reservation,
                reservation.getStudyRoom().getWorkPlace(),
                    fileLocationService
            ))
            .toList();
    }



    // 내 사업장의 예약자 보기 (예약자 확인 페이지)
    @Transactional(readOnly = true)
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(Long businessId) {
        List<Reservation> reservations = reservationRepository.findMyAllReservations(businessId);

        if(reservations.isEmpty()){
            return null;
        }

        return reservations.stream()
            .map(reservation -> MyWorkPlaceReservationResponse.from(
                reservation.getStudyRoom(),
                reservation,
                reservation.getStudyRoom().getWorkPlace(),
                    fileLocationService
            ))
            .toList();
    }
}