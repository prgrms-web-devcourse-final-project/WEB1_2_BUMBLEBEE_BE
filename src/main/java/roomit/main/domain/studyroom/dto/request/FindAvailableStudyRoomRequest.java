package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record FindAvailableStudyRoomRequest(
    @NotBlank String address,
    @NotNull @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm") LocalDateTime startDateTime,
    @NotNull @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm") LocalDateTime endDateTime,
    @NotNull Integer reservationCapacity
){

}