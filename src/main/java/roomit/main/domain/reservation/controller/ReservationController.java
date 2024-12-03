package roomit.main.domain.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
import roomit.main.domain.reservation.dto.response.ReservationResponse;
import roomit.main.domain.reservation.service.ReservationService;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 만들기
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/reservations/{studyRoomId}")
    public Map<String, Long> createReservation(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                               @PathVariable @Positive Long studyRoomId,
                                               @RequestBody @Valid CreateReservationRequest request) {
        Long reservationId = reservationService.createReservation(customMemberDetails.getId(),studyRoomId,request);
        Map<String, Long> result = new HashMap<>();
        result.put("reservationId", reservationId);
        return result;
    }

    // 예약 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/reservations/{reservationId}")
    public void deleteReservation(@PathVariable @Positive Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    // 예약 수정
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/v1/reservations/{reservationId}")
    public void updateReservation(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                  @PathVariable @Positive Long reservationId,
                                  @RequestBody @Valid UpdateReservationRequest request) {
        reservationService.updateReservation(customMemberDetails.getId(),reservationId , request);
    }

    // 특정 멤버의 예약 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/reservations/member")
    public ReservationResponse findRecentReservationByMemberId(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return reservationService.findByMemberId(customMemberDetails.getId());
    }

    // 특정 멤버의 예약 전체 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/all/reservations/member")
    public List<ReservationResponse> findReservationsByMemberId(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        return reservationService.findReservationsByMemberId(customMemberDetails.getId());
    }

    // 특정 사업장의 예약 찾기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/reservations/workplace/{workplaceId}")
    public List<MyWorkPlaceReservationResponse> findReservationByWorkplaceId(@PathVariable @Positive Long workplaceId) {
        return reservationService.findReservationByWorkplaceId(workplaceId);
    }
}
