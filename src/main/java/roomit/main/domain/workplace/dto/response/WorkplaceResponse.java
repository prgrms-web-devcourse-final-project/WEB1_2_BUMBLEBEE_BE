package roomit.main.domain.workplace.dto.response;

import roomit.main.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

public record WorkplaceResponse(
        Long workplaceId,
        String workplaceName,
        String workplacePhoneNumber,
        String workplaceAddress,
        String imageUrl,
        LocalDateTime createdAt
) {
    public WorkplaceResponse(Workplace workplace) {
        this(
            workplace.getWorkplaceId(),
            workplace.getWorkplaceName().getValue(),
            workplace.getWorkplacePhoneNumber().getValue(),
            workplace.getWorkplaceAddress().getValue(),
            workplace.getImageUrl().getValue(),
            workplace.getCreatedAt()
        );
    }
}
