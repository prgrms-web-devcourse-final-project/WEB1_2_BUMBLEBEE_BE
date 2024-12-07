package roomit.main.domain.workplace.service;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomit.main.domain.workplace.dto.request.RecommendationResponseWrapper;
import roomit.main.domain.workplace.dto.response.WorkplaceRecommendResponse;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.service.FileLocationService;

@Service
@RequiredArgsConstructor
public class WorkplaceRecommendationService {
    @Value("${AI.URL}")
    private String url ;

    private final WorkplaceRepository workplaceRepository;
    private final FileLocationService fileLocationService;


    public List<WorkplaceRecommendResponse> getRecommendations(Long userId, int age, int n) {
         // Flask 서버의 recommend 엔드포인트
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

                return response.getBody().getRecommendations().stream()
                    .map(result -> {
                        Workplace workplace = workplaceRepository.findById(result.workplaceId())
                            .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

                        Double starSum = workplace.getStarSum().doubleValue();
                        Long reviewCount = workplace.getReviewCount();

                        return new WorkplaceRecommendResponse(
                            workplace.getWorkplaceId(),
                            workplace.getWorkplaceName().getValue(),
                            workplace.getWorkplaceAddress().getValue(),
                            fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                            (reviewCount == 0) ? 0.0 : starSum / reviewCount,
                            reviewCount,
                            workplace.getLocation().getX(),
                            workplace.getLocation().getY()
                        );
                    })
                    .toList();
            } else {
                throw ErrorCode.PYTHON_CONNECTED_FAIL.commonException();
            }
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_RECOMMEND_FAIL.commonException();
        }
    }
}