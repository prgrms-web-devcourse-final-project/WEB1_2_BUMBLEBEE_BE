package roomit.main.domain.workplace.dto.request;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;
import roomit.main.domain.workplace.entity.value.Coordinate;

@Builder
public record WorkplaceGetRequest(
        Coordinate topRight,
        Coordinate bottomLeft,
        Double latitude,
        Double longitude
) {
}
