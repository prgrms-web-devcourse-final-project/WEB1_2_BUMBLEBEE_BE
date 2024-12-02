package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.entity.value.StudyRoomName;

public record UpdateStudyRoomRequest(@Pattern(regexp = StudyRoomName.REGEX, message = StudyRoomName.ERR_MSG) String studyRoomName,
                                     @NotNull (message = "스터디룸 상세 설명은 필수입니다.") String description,
                                     @NotNull (message = "스터디룸 수용인원은 필수입니다.") Integer capacity,
                                     @NotNull (message = "스터디룸 가격은 필수입니다.") Integer price
) {
    public void updatedStudyRoom(StudyRoom studyRoom) {
        studyRoom.setStudyRoomName(new StudyRoomName(studyRoomName));
        studyRoom.setDescription(description);
        studyRoom.setCapacity(capacity);
        studyRoom.setPrice(price);
    }
}