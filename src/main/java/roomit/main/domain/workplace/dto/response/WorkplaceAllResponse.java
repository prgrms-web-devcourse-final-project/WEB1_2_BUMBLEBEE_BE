package roomit.main.domain.workplace.dto.response;

public record WorkplaceAllResponse(
        Long workplaceId,
        String workplaceName,
        String workplaceAddress,
        String imageUrl,
        Double stars,
        Long reviewCount,
        Double distance
) {}

