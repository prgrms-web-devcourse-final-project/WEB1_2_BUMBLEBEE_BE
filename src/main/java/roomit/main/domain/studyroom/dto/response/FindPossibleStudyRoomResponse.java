package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.global.service.FileLocationService;

public record FindPossibleStudyRoomResponse(
    Long studyroomId,
    String workplaceName,
    String studyRoomName,
    Double reviewScore,
    Long reviewCount,
    String workplaceAddress,
    Integer studyRoomCapacity,
    Integer studyRoomPrice,
    String  imageUrl,
    Double distance
){

  public FindPossibleStudyRoomResponse (StudyRoom studyRoom, Double distance, FileLocationService fileLocationService){
    this(
        studyRoom.getStudyRoomId(),
        studyRoom.getWorkPlace().getWorkplaceName().getValue(),
        studyRoom.getStudyRoomName().getValue(),
        (double) studyRoom.getWorkPlace().getStarSum() / (studyRoom.getWorkPlace().getReviewCount()),
        studyRoom.getWorkPlace().getReviewCount(),
        studyRoom.getWorkPlace().getWorkplaceAddress().getValue(),
        studyRoom.getCapacity(),
        studyRoom.getPrice(),
        fileLocationService.getImagesFromFolder(studyRoom.getWorkPlace().getImageUrl().getValue()).get(0),
        distance
    );
  }
}
