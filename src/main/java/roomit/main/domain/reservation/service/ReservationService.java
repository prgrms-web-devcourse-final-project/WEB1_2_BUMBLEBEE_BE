package roomit.main.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.studyroom.dto.response.StudyRoomResponse;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WorkplaceRepository workplaceRepository;
    private final StudyRoomRepository studyRoomRepository;

    // memberId를 이용하여 나의 최근 예약 조회
    @Transactional(readOnly = true)
    public ReservationResponse findByMemberId(Long memberId) {
        return reservationRepository.findRecentReservationByMemberId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("예약한적이 없습니다"));
    }

    // memberId를 이용하여 나의 예약 전체 조회
    public List<ReservationResponse> findReservationsByMemberId(Long memberId){
        return reservationRepository.findReservationsByMemberId(memberId);
    }

    // 예약 만드는 메서드
    @Transactional
    public void createReservation(CreateReservationRequest request) {
        Reservation reservation = request.toEntity();
        reservationRepository.save(reservation);
    }

    // x를 눌러 예약을 삭제하는 메서드
    @Transactional
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    // 예약을 수정하는 메서드
    @Transactional
    public void updateReservation(Long reservationId, UpdateReservationRequest request) {
        Reservation existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        request.updateReservation(existingReservation);
        reservationRepository.save(existingReservation);
    }

    // 내 사업장의 예약자 보기 (예약자 확인 페이지)
    @Transactional(readOnly = true)
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(Long workplaceId) {
        return reservationRepository.findMyWorkPlaceReservationsByWorkPlaceId(workplaceId);
    }


}
