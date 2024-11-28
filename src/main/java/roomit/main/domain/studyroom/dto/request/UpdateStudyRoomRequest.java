package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import roomit.main.domain.studyroom.entity.StudyRoom;

public record UpdateStudyRoomRequest(
    @NotNull (message = "스터디룸 이름은 필수입니다.") String title,
    @NotNull (message = "스터디룸 상세 설명은 필수입니다.") String description,
    @NotNull (message = "스터디룸 수용인원은 필수입니다.") Integer capacity,
    @NotNull (message = "스터디룸 가격은 필수입니다.") Integer price
) {
    public void updatedStudyRoom(StudyRoom studyRoom) {
        studyRoom.setTitle(title);
        studyRoom.setDescription(description);
        studyRoom.setCapacity(capacity);
        studyRoom.setPrice(price);
    }
}