package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import lombok.*;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateStudyRoomRequest {
    private Long studyRoomId;
    private String title;
    private String description;
    private Integer capacity;
    private Integer price;

    @Builder
    public UpdateStudyRoomRequest(Long studyRoomId, String title, String description, Integer capacity, Integer price) {
        this.studyRoomId = studyRoomId;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
    }

    public static StudyRoom toEntity(UpdateStudyRoomRequest request, StudyRoom existingStudyRoom) {
        existingStudyRoom.setTitle(request.getTitle());
        existingStudyRoom.setDescription(request.getDescription());
        existingStudyRoom.setCapacity(request.getCapacity());
        existingStudyRoom.setPrice(request.getPrice());
        return existingStudyRoom;
    }


}