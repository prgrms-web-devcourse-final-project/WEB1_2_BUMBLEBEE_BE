package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import roomit.web1_2_bumblebee_be.domain.reservation.entity.Reservation;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RecentStudyRoomResponse(
    WorkplaceName workplaceName,
    LocalDate reservationDate,
    LocalDateTime reservationStartTime,
    String studyRoomName,
    Integer studyRoomCapacity,
    Integer studyRoomPrice
) {
    public static RecentStudyRoomResponse from(Workplace workplace, Reservation reservation, StudyRoom studyRoom) {
        return new RecentStudyRoomResponse(
                workplace.getWorkplaceName(),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                studyRoom.getTitle(),
                studyRoom.getCapacity(),
                studyRoom.getPrice()
        );
    }
}
