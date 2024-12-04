package roomit.main.domain.reservation.dto.response;

import java.time.LocalDateTime;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;

public record ReservationResponse (
        Long reservationId,
        Long workplaceId,
        String workplaceName,
        String workplaceImageUrl,
        String studyRoomName,
        LocalDateTime reservationCreatedAt,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer reservationCapacity,
        Integer price
        // LocalDateTime paymentCreatedAt
){
    public static ReservationResponse from(StudyRoom studyRoom ,Reservation reservation, Workplace workplace) {
        return new ReservationResponse(
                reservation.getReservationId(),
                workplace.getWorkplaceId(),
                workplace.getWorkplaceName().getValue(),
                workplace.getImageUrl().getValue(),
                studyRoom.getStudyRoomName().getValue(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getReservationCapacity(),
                studyRoom.getPrice()
                // payment.getCreatedAt()
        );
    }
}
