package roomit.main.domain.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 만들기
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/reservations")
    public void createReservation(@RequestBody @Valid CreateReservationRequest request) {
        reservationService.createReservation(request);
    }

    // 예약 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/reservations/{reservationId}")
    public void deleteReservation(@PathVariable @Positive Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    // 예약 수정
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/reservations/{reservationId}")
    public void updateReservation(@PathVariable @Positive Long reservationId, @RequestBody @Valid UpdateReservationRequest request) {
        reservationService.updateReservation(reservationId, request);
    }

    // 특정 멤버의 예약 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/reservations/member/{memberId}")
    public ReservationResponse findRecentReservationByMemberId(@PathVariable @Positive Long memberId) {
        return reservationService.findByMemberId(memberId);
    }

    // 특정 멤버의 예약 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/all/reservations/member/{memberId}")
    public List<ReservationResponse> findReservationsByMemberId(@PathVariable @Positive Long memberId) {
        return reservationService.findReservationsByMemberId(memberId);
    }

    // 특정 사업장의 예약 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/reservations/workplace/{workplaceId}")
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(@PathVariable @Positive Long workplaceId) {
        return reservationService.findReservationByWorkplaceId(workplaceId);
    }
}
