package roomit.main.domain.workplace.dto.request;

import lombok.Builder;
import roomit.main.domain.workplace.entity.value.Coordinate;

@Builder
public record WorkplaceGetRequest(
        Coordinate topRight,
        Coordinate bottomLeft
) {
}
