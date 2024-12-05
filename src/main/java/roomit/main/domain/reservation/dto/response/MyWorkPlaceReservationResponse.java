package roomit.main.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.value.ReservationNum;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

public record MyWorkPlaceReservationResponse (
    WorkplaceName workplaceName,
    String reservationName,
    ReservationNum reservationPhoneNumber,
    String studyRoomName,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationCreatedAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationStartTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationEndTime,
    Integer reservationCapacity,
    String studyRoomUrl
    // LocalDateTime paymentCreatedAt
) {
    public static MyWorkPlaceReservationResponse from(StudyRoom studyRoom, Reservation reservation, Workplace workplace) {
        return new MyWorkPlaceReservationResponse(
                workplace.getWorkplaceName(),
                reservation.getReservationName().getValue(),
                reservation.getReservationPhoneNumber(),
                studyRoom.getStudyRoomName().getValue(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getReservationCapacity(),
                studyRoom.getImageUrl().getValue()
                //payment.getCreatedAt()
                );
    }
}