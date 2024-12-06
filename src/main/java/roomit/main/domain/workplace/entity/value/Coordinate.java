package roomit.main.domain.workplace.entity.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Coordinate {
    @JsonProperty("lat") private Double latitude;
    @JsonProperty("lng") private Double longitude;

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
