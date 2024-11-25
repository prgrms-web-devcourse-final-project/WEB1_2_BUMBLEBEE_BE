package roomit.web1_2_bumblebee_be.domain.studyroom.dto.response;

import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;

public record StudyRoomResponse (
    String title,
    String description,
    String imageUrl,
    Integer price,
    Integer capacity
){

    public static StudyRoomResponse from(StudyRoom studyRoom) {
        return new StudyRoomResponse(
                studyRoom.getTitle(),
                studyRoom.getDescription(),
                studyRoom.getImageUrl(),
                studyRoom.getPrice(),
                studyRoom.getCapacity()
                );
    }
}