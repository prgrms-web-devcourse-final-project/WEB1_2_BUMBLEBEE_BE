package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

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

    @Builder
    public StudyRoomRequest(String title, String description, String locaion, Integer num){
        this.title = title;
        this.description = description;
        this.locaion = locaion;
        this.num = num;
    }
}
