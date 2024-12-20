package roomit.main.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.entity.value.ReservationName;
import roomit.main.domain.reservation.entity.value.ReservationNum;
import roomit.main.domain.studyroom.entity.StudyRoom;

import java.time.LocalDateTime;

@Builder
public record CreateReservationRequest (

        @Pattern(regexp = ReservationName.REGEX, message = ReservationName.ERR_MSG) String reservationName,
        @Pattern(regexp = ReservationNum.REGEX, message = ReservationNum.ERR_MSG) String reservationPhoneNumber,
        @NotNull Integer capacity,
        @NotNull Integer price,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {
    public Reservation toEntity(Member member, StudyRoom studyRoom) {
        return Reservation.builder()
                .reservationName(reservationName)
                .reservationPhoneNumber(reservationPhoneNumber)
                .reservationState(ReservationState.ON_HOLD)
                .reservationCapacity(capacity)
                .reservationPrice(price)
                .startTime(startTime)
                .endTime(endTime)
                .member(member)
                .studyRoom(studyRoom)
                .build();
    }
}