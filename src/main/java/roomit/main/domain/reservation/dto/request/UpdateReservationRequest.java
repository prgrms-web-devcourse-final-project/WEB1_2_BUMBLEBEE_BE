package roomit.main.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomit.main.domain.reservation.entity.value.ReservationName;
import roomit.main.domain.reservation.entity.value.ReservationNum;

import java.time.LocalDateTime;

public record UpdateReservationRequest (

    @Pattern(regexp = ReservationName.REGEX, message = ReservationName.ERR_MSG) String reservationName,

    @Pattern(regexp = ReservationNum.REGEX, message = ReservationNum.ERR_MSG) String reservationPhoneNumber,

    @NotNull LocalDateTime startTime,

    @NotNull LocalDateTime endTime
){
}