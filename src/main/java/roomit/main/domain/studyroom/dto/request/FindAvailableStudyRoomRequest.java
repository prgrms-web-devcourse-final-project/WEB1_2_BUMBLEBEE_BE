package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record FindAvailableStudyRoomRequest(
    @NotBlank String address,
    @NotNull @DateTimeFormat(pattern = "HH:mm") String startTime,
    @NotNull @DateTimeFormat(pattern = "HH:mm") String endTime,
    @NotBlank Integer capacity
){

}