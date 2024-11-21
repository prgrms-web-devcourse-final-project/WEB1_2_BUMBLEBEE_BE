package roomit.web1_2_bumblebee_be.domain.studyroom.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateStudyRoomRequest {
    private String title;
    private String description;
    private Integer capacity;
    private Integer price;
    private String imageUrl;
    private Long workplaceId;

    @Builder
    public CreateStudyRoomRequest(String title, String description, Integer capacity, Integer price, String imageUrl, Long workplaceId) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.workplaceId = workplaceId;
    }

    public StudyRoom toEntity(Workplace workplace) {
        return StudyRoom.builder()
                .title(this.title)
                .description(this.description)
                .capacity(this.capacity)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .workPlaceId(workplace)
                .build();
    }

}