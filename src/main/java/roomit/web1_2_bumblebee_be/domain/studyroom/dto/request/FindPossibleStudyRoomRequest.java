package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPossibleStudyRoomRequest {
    private String workplaceAddress;
    private String startTime;
    private String endTime;
    private Integer capacity;

    @Builder
    public FindPossibleStudyRoomRequest(String workplaceAddress, String startTime,String endTime, Integer capacity) {
        this.workplaceAddress = workplaceAddress;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }
}
