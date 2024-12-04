package roomit.main.domain.studyroom.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.entity.value.StudyRoomName;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.inner.ImageUrl;
import roomit.main.global.service.ImageService;

public record CreateStudyRoomRequest(@Pattern(regexp = StudyRoomName.REGEX, message = StudyRoomName.ERR_MSG) String studyRoomName,
                                     @NotNull @Size(max = 200) String description,
                                     @Pattern(regexp = ImageUrl.REGEX, message = ImageUrl.ERR_MSG) String imageUrl,
                                     @NotNull Integer price,
                                     @NotNull Integer capacity

) {
    public StudyRoom toEntity(ImageService imageService, Workplace workplace) {

        ImageUrl image = imageService.createImageUrl(imageUrl);

        return StudyRoom.builder()
            .studyRoomName(studyRoomName)
            .description(description)
            .capacity(capacity)
            .price(price)
            .imageUrl(image)
            .workplace(workplace)
            .build();
    }
}