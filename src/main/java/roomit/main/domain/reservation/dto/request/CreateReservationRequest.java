package roomit.main.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.value.ReservationNum;

import java.time.LocalDateTime;

public record CreateReservationRequest (
    @NotBlank String reservationName,
    @Pattern(regexp = ReservationNum.REGEX, message = ReservationNum.ERR_MSG) String reservationPhoneNumber,
    @NotNull LocalDateTime startTime,
    @NotNull LocalDateTime endTime
) {
    public Reservation toEntity() {
        return Reservation.builder()
                .reservationName(this.reservationName)
                .reservationPhoneNumber(this.reservationPhoneNumber)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }
}