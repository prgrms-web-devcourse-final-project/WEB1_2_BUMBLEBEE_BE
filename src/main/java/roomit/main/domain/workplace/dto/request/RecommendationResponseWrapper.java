package roomit.main.domain.workplace.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseWrapper{
  @JsonProperty("age_group") String ageGroup; // Flask에서 반환하는 연령대 정보
  List<RecommendationResponse> recommendations;

}