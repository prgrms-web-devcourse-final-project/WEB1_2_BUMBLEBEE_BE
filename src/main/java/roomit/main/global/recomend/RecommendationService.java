package roomit.main.global.recomend;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    public RecommendationResponseWrapper getRecommendations(Long userId, int age, int n) {
        String url = "http://127.0.0.1:8070/recommend"; // Flask 서버의 recommend 엔드포인트
        RestTemplate restTemplate = new RestTemplate();

        // Flask로 보낼 요청 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", userId);
        requestBody.put("age", age);
        requestBody.put("n", n);

        try {
            // Flask 서버에 POST 요청
            ResponseEntity<RecommendationResponseWrapper> response = restTemplate.postForEntity(
                    url, requestBody, RecommendationResponseWrapper.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // 성공 시 추천 결과 반환
            } else {
                throw new RuntimeException("Failed to get recommendations from Flask. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during Flask communication: " + e.getMessage());
        }
    }

    @Data
    public static class RecommendationResponseWrapper {
        @JsonProperty("age_group")
        private String ageGroup; // Flask에서 반환하는 연령대 정보
        private List<RecommendationResponse> recommendations;
    }

    @Data
    public static class RecommendationResponse {
        @JsonProperty("workplace_id")
        private Long workplaceId;

        @JsonProperty("workplace_name")
        private String workplaceName;

        @JsonProperty("predicted_rating")
        private double predictedRating;
    }
}