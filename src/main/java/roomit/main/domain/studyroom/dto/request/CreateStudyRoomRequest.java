package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomit.main.domain.studyroom.entity.StudyRoom;

public record CreateStudyRoomRequest(
        @NotBlank String title,
        @NotBlank @Size(max = 200) String description,
        String imageUrl,
        @NotBlank Integer price,
        @NotBlank Integer capacity

) {

    public StudyRoom toEntity() {
        return StudyRoom.builder()
                .title(this.title)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .price(this.price)
                .capacity(this.capacity)
                .build();
    }
}