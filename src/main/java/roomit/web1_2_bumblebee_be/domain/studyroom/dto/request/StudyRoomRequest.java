package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoomRequest {
    @NotBlank(message = "스터디룸 이름은 필수입니다.")
    private  String title;
    @NotBlank(message = "스터디룸 설명은 필수입니다.")
    private String description;
    @NotNull(message = "스터디룸 수용인원은 필수입니다.")
    private Integer capacity;
    @NotNull(message = "금액은 필수입니다.")
    private Integer price;

    @Builder
    public StudyRoomRequest(String title, String description, Integer capacity,Integer price) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
    }

    // toEntity 메서드 추가
    public StudyRoom toEntity() {
        return StudyRoom.builder()
                .title(title)
                .description(description)
                .capacity(capacity)
                .price(price)
                .build();
    }
}