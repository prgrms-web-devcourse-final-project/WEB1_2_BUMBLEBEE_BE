package roomit.main.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.service.FileLocationService;

public record ReservationResponse (
        Long reservationId,
        Long workplaceId,
        String workplaceName,
        String workplaceImageUrl,
        String studyRoomName,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reservationCreatedAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
        Integer reservationCapacity,
        Integer price,
        Boolean existReview
        // LocalDateTime paymentCreatedAt
){
    public static ReservationResponse from(StudyRoom studyRoom , Reservation reservation, Workplace workplace, FileLocationService fileLocationService) {
        return new ReservationResponse(
                reservation.getReservationId(),
                workplace.getWorkplaceId(),
                workplace.getWorkplaceName().getValue(),
                fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                studyRoom.getStudyRoomName().getValue(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getReservationCapacity(),
                studyRoom.getPrice(),
                reservation.getReview() != null
                // payment.getCreatedAt()
        );
    }
}