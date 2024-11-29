package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import roomit.main.domain.studyroom.entity.StudyRoom;

public record CreateStudyRoomRequest(
        @NotNull @Size(max = 100) String title,
        @NotNull @Size(max = 200) String description,
        String imageUrl,
        @NotNull Integer price,
        @NotNull Integer capacity

) {
    public StudyRoom toEntity() {
        StudyRoom studyRoom = new StudyRoom();
        studyRoom.setTitle(this.title);
        studyRoom.setDescription(this.description);
        studyRoom.setCapacity(this.capacity);
        studyRoom.setPrice(this.price);
        studyRoom.setImageUrl(this.imageUrl);
        return studyRoom;
    }
}