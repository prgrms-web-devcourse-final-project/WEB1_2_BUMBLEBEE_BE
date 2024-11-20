package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudyRoomRequest {
    @NotBlank(message = "스터디룸 이름은 필수입니다.")
    private String title;

    @NotBlank(message = "스터디룸 설명은 필수입니다.")
    private String description;

    @NotBlank(message = "스터디룸 주소는 필수입니다.")
    private String locaion;

    @NotBlank(message = "스터디룸 수용인원은 필수입니다.")
    private Integer num;

}
