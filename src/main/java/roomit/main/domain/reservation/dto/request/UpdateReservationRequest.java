package roomit.main.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomit.main.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;

public record UpdateReservationRequest (

    @NotBlank String reservationName,

    @NotBlank String reservationPhoneNumber,

    @NotNull LocalDateTime startTime,

    @NotNull LocalDateTime endTime
){
    public void updateReservation(Reservation reservation) {
        reservation.setReservationName(this.reservationName);
        reservation.setReservationPhoneNumber(this.reservationPhoneNumber);
        reservation.setStartTime(this.startTime);
        reservation.setEndTime(this.endTime);
    }

}