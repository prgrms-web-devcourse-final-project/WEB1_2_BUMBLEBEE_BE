package roomit.main.domain.studyroom.dto.response;

import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import roomit.main.domain.studyroom.entity.StudyRoom;

public record ReservationPossibleStudyRoomResponse(
    Integer capacity,                                         //예약 가능한 인원수
    List<String> possibleTime,                                //예약 가능한 시간
    @DateTimeFormat(pattern = "HH:mm") String startTime,      //영업 시작 시간
    @DateTimeFormat(pattern = "HH:mm") String endTime         //영업 종료 시간
){

  public ReservationPossibleStudyRoomResponse(StudyRoom studyRoom,
                                              List<String> possibleTime,
                                              LocalTime startTime,
                                              LocalTime endTime) {
    this(
        studyRoom.getCapacity(),
        possibleTime,
        startTime.toString(),
        endTime.toString()
    );
  }
}


