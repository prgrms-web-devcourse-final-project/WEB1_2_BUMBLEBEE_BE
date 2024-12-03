package roomit.main.domain.studyroom.dto.response;

import roomit.main.domain.studyroom.entity.StudyRoom;

public record StudyRoomResponse (
    String title,
    String description,
    String imageUrl,
    Integer price,
    Integer capacity
){

    public static StudyRoomResponse from(StudyRoom studyRoom) {
        return new StudyRoomResponse(
                studyRoom.getStudyRoomName().getValue(),
                studyRoom.getDescription(),
                studyRoom.getImageUrl().getValue(),
                studyRoom.getPrice(),
                studyRoom.getCapacity()
                );
    }
}