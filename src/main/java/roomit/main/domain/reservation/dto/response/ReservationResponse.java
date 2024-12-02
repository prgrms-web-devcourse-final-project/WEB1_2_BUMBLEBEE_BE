package roomit.main.domain.reservation.dto.response;

import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

import java.time.LocalDateTime;

public record ReservationResponse (
        WorkplaceName workplaceName,
        LocalDateTime reservationCreatedAt,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer studyRoomCapacity,
        Integer price,
        String studyRoomUrl
        // LocalDateTime paymentCreatedAt
){
    public static ReservationResponse from(StudyRoom studyRoom ,Reservation reservation, Workplace workplace) {
        return new ReservationResponse(
                workplace.getWorkplaceName(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                studyRoom.getCapacity(),
                studyRoom.getPrice(),
                studyRoom.getImageUrl().getValue()
                // payment.getCreatedAt()
        );
    }
}
