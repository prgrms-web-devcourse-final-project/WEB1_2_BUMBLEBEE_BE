package roomit.main.domain.workplace.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendationResponse(
    @JsonProperty("workplace_id") Long workplaceId,
    @JsonProperty("workplace_name") String workplaceName,
    @JsonProperty("predicted_rating") Double predictedRating,
    @JsonProperty("final_score") Double finalScore // 최종 점수 추가
) {

}
