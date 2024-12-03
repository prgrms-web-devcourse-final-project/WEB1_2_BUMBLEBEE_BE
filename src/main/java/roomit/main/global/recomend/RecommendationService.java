package roomit.main.global.recomend;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RecommendationService {

    public List<RecommendationResponse> getRecommendations(Long userId, int n) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://127.0.0.1:8070/recommend";

        // 요청 데이터 생성
        JSONObject requestBody = new JSONObject();
        requestBody.put("user_id", userId);
        requestBody.put("n", n); // Top-N 개수를 전달

        // HTTP 요청 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // Flask 서버로 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // JSON 응답을 리스트 형태로 변환
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(response.getBody(), new TypeReference<List<RecommendationResponse>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Error parsing Flask response: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Error during Flask communication: " + response.getStatusCode() + ": " + response.getBody());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationResponse {
        private Long workplace_id;
        private String workplace_name;
        private Double predicted_rating;
    }
}