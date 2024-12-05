package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.entity.value.StudyRoomName;
import roomit.main.domain.workplace.entity.Workplace;

public record CreateStudyRoomRequest(@Pattern(regexp = StudyRoomName.REGEX, message = StudyRoomName.ERR_MSG) String studyRoomName,
                                     @NotNull @Size(max = 200) String description,
                                     @NotNull Integer price,
                                     @NotNull Integer capacity

) {
    public StudyRoom toEntity(Workplace workplace) {

        return StudyRoom.builder()
            .studyRoomName(studyRoomName)
            .description(description)
            .capacity(capacity)
            .price(price)
            .workplace(workplace)
            .build();
    }
}