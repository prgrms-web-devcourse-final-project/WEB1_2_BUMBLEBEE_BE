package roomit.main.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.entity.value.ReservationNum;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.global.service.FileLocationService;

public record MyWorkPlaceReservationResponse (
    String  workplaceName,
    String reservationName,
    String  reservationPhoneNumber,
    String studyRoomName,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationCreatedAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationStartTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationEndTime,
    Integer reservationCapacity,
    String workplaceImageUrl,
    Long workplaceId,
    Long reservationId,
    Integer reservationPrice,
    ReservationState reservationState
    // LocalDateTime paymentCreatedAt
) {
    public static MyWorkPlaceReservationResponse from(StudyRoom studyRoom, Reservation reservation, Workplace workplace, FileLocationService fileLocationService) {
        return new MyWorkPlaceReservationResponse(
                workplace.getWorkplaceName().getValue(),
                reservation.getReservationName().getValue(),
                reservation.getReservationPhoneNumber().getValue(),
                studyRoom.getStudyRoomName().getValue(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getReservationCapacity(),
                fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                workplace.getWorkplaceId(),
                reservation.getReservationId(),
                reservation.getReservationPrice(),
                reservation.getReservationState()
                //payment.getCreatedAt()
                );
    }
}