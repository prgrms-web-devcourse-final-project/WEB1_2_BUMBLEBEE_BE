package roomit.main.domain.workplace.dto.response;

import java.time.LocalDateTime;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.service.FileLocationService;

public record WorkplaceResponse(
        Long workplaceId,
        String workplaceName,
        String workplacePhoneNumber,
        String workplaceAddress,
        String imageUrl,
        Long studyRoomCount,
        LocalDateTime createdAt
) {
    public WorkplaceResponse(Workplace workplace, FileLocationService fileLocationService) {
        this(
                workplace.getWorkplaceId(),
                workplace.getWorkplaceName().getValue(),
                workplace.getWorkplacePhoneNumber().getValue(),
                workplace.getWorkplaceAddress().getValue(),
                fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                (long) workplace.getStudyRoom().size(),
                workplace.getCreatedAt()
        );
    }
}
