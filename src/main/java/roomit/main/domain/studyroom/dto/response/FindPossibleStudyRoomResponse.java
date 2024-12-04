package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.studyroom.entity.StudyRoom;

public record FindPossibleStudyRoomResponse(
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

  public FindPossibleStudyRoomResponse (StudyRoom studyRoom, Double distance){
    this(
        studyRoom.getWorkPlace().getWorkplaceName().getValue(),
        studyRoom.getStudyRoomName().getValue(),
        (double) studyRoom.getWorkPlace().getStarSum() / (studyRoom.getWorkPlace().getReviewCount()),
        studyRoom.getWorkPlace().getReviewCount(),
        studyRoom.getWorkPlace().getWorkplaceAddress().getValue(),
        studyRoom.getCapacity(),
        studyRoom.getPrice(),
        studyRoom.getWorkPlace().getImageUrl().getValue(),
        distance
    );
  }
}
