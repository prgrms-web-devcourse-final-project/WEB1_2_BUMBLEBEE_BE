package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

public record UpdateStudyRoomRequest(
    @NotBlank Long studyRoomId,
    @NotBlank (message = "스터디룸 이름은 필수입니다.") String title,
    @NotBlank (message = "스터디룸 상세 설명은 필수입니다.") String description,
    @NotBlank (message = "스터디룸 수용인원은 필수입니다.") Integer capacity,
    @NotBlank (message = "스터디룸 가격은 필수입니다.") Integer price
) {
    public StudyRoom toEntity() {
        return StudyRoom.builder()
                .title(title)
                .description(description)
                .capacity(capacity)
                .price(price)
                .build();
    }

    public void updaedStudyRoom(StudyRoom studyRoom) {
        studyRoom.setTitle(title);
        studyRoom.setDescription(description);
        studyRoom.setCapacity(capacity);
        studyRoom.setPrice(price);
    }
}