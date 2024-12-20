package roomit.main.domain.studyroom.dto.response;

import java.util.List;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.global.service.FileLocationService;

public record StudyRoomResponse(
    Long studyRoomId,
    Long workplaceId,
    String workplaceName,
    String studyRoomName,
    String description,
    List<String> imageUrl,
    Integer price,
    Integer capacity
){

    public StudyRoomResponse(StudyRoom studyRoom, FileLocationService fileLocationService) {
        this(
            studyRoom.getStudyRoomId(),
            studyRoom.getWorkPlace().getWorkplaceId(),
            studyRoom.getWorkPlace().getWorkplaceName().getValue(),
            studyRoom.getStudyRoomName().getValue(),
            studyRoom.getDescription(),
            fileLocationService.getImagesFromFolder(studyRoom.getImageUrl().getValue()),
            studyRoom.getPrice(),
            studyRoom.getCapacity()
        );
    }
}
