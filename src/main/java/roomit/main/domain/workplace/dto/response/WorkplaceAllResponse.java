package roomit.main.domain.workplace.dto.response;

import java.math.BigDecimal;

public record WorkplaceAllResponse(
        Long workplaceId,
        String workplaceName,
        String workplaceAddress,
        String imageUrl,
        Double stars,
        Long reviewCount,
        Double positionLon,
        Double positionLat,
        Double distance
) {}

