package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import lombok.*;
import org.springframework.cglib.core.Local;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyroomEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class StudyRoomResponse {

    private Long studyroomId;

    private String title;

    private String description;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedat;

    public StudyRoomResponse(StudyroomEntity studyroom){
        this.studyroomId = studyroom.getStudyroomId();
        this.title = studyroom.getTitle();
        this.description = studyroom.getDescription();
        this.location = studyroom.getLocation();
        this.createdAt = studyroom.getCreatedAt();
        this.updatedat = studyroom.getUpdatedAt();
    }
}
