package roomit.main.domain.workplace.entity.value;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Coordinate {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
