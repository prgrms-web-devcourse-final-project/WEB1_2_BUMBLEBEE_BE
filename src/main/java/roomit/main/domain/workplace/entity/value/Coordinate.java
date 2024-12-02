package roomit.main.domain.workplace.entity.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Coordinate {
    @JsonProperty("lat") private BigDecimal latitude;
    @JsonProperty("lng") private BigDecimal longitude;
}
