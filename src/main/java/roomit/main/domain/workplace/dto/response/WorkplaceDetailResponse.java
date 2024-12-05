package roomit.main.domain.workplace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.service.FileLocationService;

public record WorkplaceDetailResponse(
        Long workplaceId,
        String workplaceName,
        String workplacePhoneNumber,
        String workplaceDescription,
        String workplaceAddress,
        String imageUrl,
        @JsonFormat(pattern = "HH:mm") LocalTime workplaceStartTime,
        @JsonFormat(pattern = "HH:mm") LocalTime workplaceEndTime,
        BigDecimal latitude,
        BigDecimal longitude,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt
){
    public WorkplaceDetailResponse(Workplace workplace, FileLocationService fileLocationService) {
        this(
                workplace.getWorkplaceId(),
                workplace.getWorkplaceName().getValue(),
                workplace.getWorkplacePhoneNumber().getValue(),
                workplace.getWorkplaceDescription(),
                workplace.getWorkplaceAddress().getValue(),
                fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                workplace.getWorkplaceStartTime(),
                workplace.getWorkplaceEndTime(),
                workplace.getLatitude(),
                workplace.getLongitude(),
                workplace.getCreatedAt()
        );
    }
}
