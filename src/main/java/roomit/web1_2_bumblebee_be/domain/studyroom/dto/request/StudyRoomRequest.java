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
    private String location;
    @NotBlank(message = "스터디룸 수용인원은 필수입니다.")
    private Integer capacity;
    @NotBlank(message = "금액은 필수입니다.")
    private Integer price;

    @Builder
    public StudyRoomRequest(String title, String description, String location, Integer capacity,Integer price) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.price = price;
    }
}