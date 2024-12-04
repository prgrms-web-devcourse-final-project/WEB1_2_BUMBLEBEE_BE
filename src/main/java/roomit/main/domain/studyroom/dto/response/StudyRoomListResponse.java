package roomit.main.domain.studyroom.dto.response;

import java.util.List;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.global.service.FileLocationService;

public record StudyRoomListResponse(
    Long studyRoomId,
    String studyRoomName,
    String description,
    List<String> imageUrl,
    Integer price,
    Integer capacity
){

  public StudyRoomListResponse(StudyRoom studyRoom, FileLocationService fileLocationService) {
    this(
        studyRoom.getStudyRoomId(),
        studyRoom.getStudyRoomName().getValue(),
        studyRoom.getDescription(),
        fileLocationService.getImagesFromFolder(studyRoom.getImageUrl().getValue()),
        studyRoom.getPrice(),
        studyRoom.getCapacity()
    );
  }
}