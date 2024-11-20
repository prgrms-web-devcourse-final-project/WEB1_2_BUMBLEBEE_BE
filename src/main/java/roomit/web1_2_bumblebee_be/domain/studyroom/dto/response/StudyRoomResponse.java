package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoomResponse {

    private Long studyRoomId; // 변수명 변경
    private String title;
    private String description;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudyRoomResponse(StudyRoom studyRoom) {
        setStudyRoomFields(studyRoom); // 메소드 추출
    }

    private void setStudyRoomFields(StudyRoom studyRoom) {
        this.studyRoomId = studyRoom.getStudyroomId();
        this.title = studyRoom.getTitle();
        this.description = studyRoom.getDescription();
        this.location = studyRoom.getLocation();
        this.createdAt = studyRoom.getCreatedAt();
        this.updatedAt = studyRoom.getUpdatedAt();
    }
}