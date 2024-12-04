package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.global.service.FileLocationService;

public record StudyRoomListResponse(
    Long studyRoomId,
    String studyRoomName,
    String description,
    String imageUrl,
    Integer price,
    Integer capacity
){

  public StudyRoomListResponse(StudyRoom studyRoom, FileLocationService fileLocationService) {
    this(
        studyRoom.getStudyRoomId(),
        studyRoom.getStudyRoomName().getValue(),
        studyRoom.getDescription(),
        fileLocationService.getImagesFromFolder(studyRoom.getImageUrl().getValue()).get(0),
        studyRoom.getPrice(),
        studyRoom.getCapacity()
    );
  }
}