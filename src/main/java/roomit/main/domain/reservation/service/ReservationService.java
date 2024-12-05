package roomit.main.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.qos.logback.core.joran.event.StartEvent;
import com.zaxxer.hikari.util.ClockSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.notification.dto.ResponseNotificationDto;
import roomit.main.domain.notification.entity.Notification;
import roomit.main.domain.notification.entity.NotificationType;
import roomit.main.domain.notification.repository.NotificationRepository;
import roomit.main.domain.notification.service.NotificationService;
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
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.service.FileLocationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationRepository reservationRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final WorkplaceRepository workplaceRepository;
    private final FileLocationService fileLocationService;

    // 예약 만드는 메서드
    @Transactional
    public Long createReservation(Long memberId,Long studyRoomId,CreateReservationRequest request) {
        validateReservation(request.startTime(),request.endTime());


        checkReservationTime(request.startTime(),request.endTime(),studyRoomId,ReservationState.ACTIVE,ReservationState.ON_HOLD);

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

//    @Transactional(readOnly = true)
//    public void checkReservationTime(LocalDateTime startTime, LocalDateTime endTime, Long studyRoomId, ReservationState state) {
//        String redisKey = "studyroom:" + studyRoomId + ":reservations";
//        List<Map<String, LocalDateTime>> reservations = (List<Map<String, LocalDateTime>>) redisTemplate.opsForValue().get(redisKey);
//
//        // Redis에 데이터 없으면 DB에서 불러와서 저장
//        if (reservations == null) {
//            StudyRoom existingStudyRoom = studyRoomRepository.findById(studyRoomId)
//                .orElseThrow(ErrorCode.STUDYROOM_NOT_FOUND::commonException);
//
//            reservations = existingStudyRoom.getReservations().stream()
//                .filter(reservation -> reservation.getReservationState().equals(state))
//                .map(reservation -> Map.of("startTime", reservation.getStartTime(), "endTime", reservation.getEndTime()))
//                .collect(Collectors.toList());
//
//            // Redis에 저장
//            redisTemplate.opsForValue().set(redisKey, reservations);
//        }
//
//        // Redis 데이터로 중복 검사
//        for (Map<String, LocalDateTime> res : reservations) {
//            if (res.get("startTime").isBefore(endTime) && res.get("endTime").isAfter(startTime)) {
//                throw ErrorCode.DUPLICATE_RESERVATION.commonException();
//            }
//        }
//    }

    // 예약의 중복 시간 체크
    @Transactional(readOnly = true)
    public void checkReservationTime(LocalDateTime StartTime, LocalDateTime endTime, Long studyRoomId ,ReservationState ACTIVE,ReservationState ON_HOLD) {
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

    // 제일 최근 예약 1건 조회
    @Transactional(readOnly = true)
    public ReservationResponse findByMemberId(Long memberId) {
        Reservation reservation = reservationRepository.findTopByMemberMemberIdOrderByCreatedAtDesc(memberId)
                .orElseThrow(ErrorCode.RESERVATION_IS_EMPTY::commonException);

        StudyRoom studyRoom = reservation.getStudyRoom();
        Workplace workPlace = studyRoom.getWorkPlace();

        return ReservationResponse.from(studyRoom,reservation,workPlace,fileLocationService);
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
            throw(ErrorCode.RESERVATION_IS_EMPTY.commonException());
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