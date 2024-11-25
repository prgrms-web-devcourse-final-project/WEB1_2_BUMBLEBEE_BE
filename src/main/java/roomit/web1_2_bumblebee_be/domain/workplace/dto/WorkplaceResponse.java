package roomit.web1_2_bumblebee_be.domain.workplace.dto;

import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WorkplaceResponse (
        Long businessId,
        String workplaceName,
        String workplacePhoneNumber,
        String workplaceDescription,
        String workplaceAddress,
        String imageUrl,
        LocalDateTime workplaceStartTime,
        LocalDateTime workplaceEndTime,
        LocalDateTime createdAt,
        BigDecimal longitude,
        BigDecimal latitude
){
    public WorkplaceResponse(Workplace workplace) {
        this(
                workplace.getBusiness().getBusinessId(),
                workplace.getWorkplaceName().getValue(),
                workplace.getWorkplacePhoneNumber().getValue(),
                workplace.getWorkplaceDescription(),
                workplace.getWorkplaceAddress().getValue(),
                workplace.getImageUrl().getValue(),
                workplace.getWorkplaceStartTime(),
                workplace.getWorkplaceEndTime(),
                workplace.getCreatedAt(),
                workplace.getLongitude(),
                workplace.getLatitude()
        );
    }
}
