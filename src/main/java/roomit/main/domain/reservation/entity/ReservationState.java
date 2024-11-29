package roomit.main.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationState {
    RESERVABLE("예약 가능"),
    CANCELLED("예약 취소"),
    COMPLETED("예약 완료");

    private final String description;

}