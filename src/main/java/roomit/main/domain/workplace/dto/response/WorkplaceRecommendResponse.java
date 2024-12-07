package roomit.main.domain.workplace.dto.response;

public record WorkplaceRecommendResponse(
        Long workplaceId,
        String workplaceName,
        String workplaceAddress,
        String imageUrl,
        Double stars,
        Long reviewCount,
        Double positionLon,
        Double positionLat
) {}

