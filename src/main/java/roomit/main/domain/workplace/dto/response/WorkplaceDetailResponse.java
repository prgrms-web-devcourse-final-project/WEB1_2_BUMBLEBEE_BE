package roomit.main.domain.workplace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomit.main.domain.workplace.entity.Workplace;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        LocalDateTime createdAt
){
    public WorkplaceDetailResponse(Workplace workplace) {
        this(
                workplace.getWorkplaceId(),
                workplace.getWorkplaceName().getValue(),
                workplace.getWorkplacePhoneNumber().getValue(),
                workplace.getWorkplaceDescription(),
                workplace.getWorkplaceAddress().getValue(),
                workplace.getImageUrl().getValue(),
                workplace.getWorkplaceStartTime(),
                workplace.getWorkplaceEndTime(),
                workplace.getLatitude(),
                workplace.getLongitude(),
                workplace.getCreatedAt()
        );
    }
}
