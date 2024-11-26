package roomit.main.domain.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomit.main.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;

public record CreateReservationRequest (
    @NotBlank String reservationName,
    @NotBlank String reservationPhoneNumber,
    @NotBlank LocalDateTime startTime,
    @NotBlank LocalDateTime endTime
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