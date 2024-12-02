package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.entity.value.WorkplaceName;

import java.time.LocalDateTime;

public record RecentStudyRoomResponse(
    WorkplaceName workplaceName,
    LocalDateTime reservationDate,
    LocalDateTime reservationStartTime,
    String studyRoomName,
    Integer studyRoomCapacity,
    Integer studyRoomPrice
) {
    public static RecentStudyRoomResponse from(Workplace workplace, Reservation reservation, StudyRoom studyRoom) {
        return new RecentStudyRoomResponse(
                workplace.getWorkplaceName(),
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                studyRoom.getStudyRoomName().getValue(),
                studyRoom.getCapacity(),
                studyRoom.getPrice()
        );
    }
}
