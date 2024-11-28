package roomit.main.domain.workplace.dto.reponse;

import lombok.Builder;
import roomit.main.domain.workplace.entity.value.Coordinate;

@Builder
public record WorkplaceGetRequest(
        Coordinate topLeft,
        Coordinate bottomRight
) {
}
