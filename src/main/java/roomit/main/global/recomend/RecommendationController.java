package roomit.main.global.recomend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<List<RecommendationService.RecommendationResponse>> getRecommendations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int n // 기본 Top-5 추천
    ) {
        try {
            List<RecommendationService.RecommendationResponse> recommendations =
                    recommendationService.getRecommendations(userId, n);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error fetching recommendations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}