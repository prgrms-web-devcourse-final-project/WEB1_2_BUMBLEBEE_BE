package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomit.main.domain.studyroom.entity.StudyRoom;

public record StudyRoomRequest(
        @NotBlank (message = "스터디룸 이름은 필수입니다.") String title,
        @NotBlank @Size(max = 200, message = "스터디룸 설명은 필수입니다.") String description,
        @NotBlank (message = "스터디룸 수용인원은 필수입니다.") Integer capacity,
        @NotBlank (message = "금액은 필수입니다.") Integer price
) {
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