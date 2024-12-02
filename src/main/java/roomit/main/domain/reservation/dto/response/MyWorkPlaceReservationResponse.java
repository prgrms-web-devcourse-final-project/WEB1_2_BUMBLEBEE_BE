package roomit.main.domain.reservation.dto.response;

import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

import java.time.LocalDateTime;

public record MyWorkPlaceReservationResponse (
    WorkplaceName workplaceName,
    String reservationName,
    String reservationPhoneNumber,
    String studyRoomName,
    LocalDateTime reservationCreatedAt,
    LocalDateTime reservationStartTime,
    LocalDateTime reservationEndTime,
    Integer studyRoomCapacity,
    String studyRoomUrl
    // LocalDateTime paymentCreatedAt
) {
    public static MyWorkPlaceReservationResponse from(StudyRoom studyRoom, Reservation reservation, Workplace workplace) {
        return new MyWorkPlaceReservationResponse(
                workplace.getWorkplaceName(),
                reservation.getReservationName(),
                reservation.getReservationPhoneNumber(),
                studyRoom.getStudyRoomName().getValue(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                studyRoom.getCapacity(),
                studyRoom.getImageUrl().getValue()
                //payment.getCreatedAt()
                );
    }
}