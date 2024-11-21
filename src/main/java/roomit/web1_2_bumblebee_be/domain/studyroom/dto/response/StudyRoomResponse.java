package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoomResponse {

    private Long studyRoomId; // 변수명 변경
    private String title;
    private String description;


    private void setStudyRoomFields(StudyRoom studyRoom) {
        this.studyRoomId = studyRoom.getStudyroomId();
        this.title = studyRoom.getTitle();
        this.description = studyRoom.getDescription();
    }
}