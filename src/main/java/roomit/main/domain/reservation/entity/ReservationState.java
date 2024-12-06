package roomit.main.domain.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationState {
    PAYMENT_FAIL("결제 실패"),
    ON_HOLD("예약 대기"),
    CANCELLED("예약 취소"),
    COMPLETED("예약 완료"),
    ACTIVE("활성"),
    INITIAL("미정");

    private final String description;

}