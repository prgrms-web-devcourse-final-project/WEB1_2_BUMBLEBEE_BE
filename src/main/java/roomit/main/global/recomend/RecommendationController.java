package roomit.main.global.recomend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/recommend")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<RecommendationService.RecommendationResponseWrapper> getRecommendations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int n // 기본 Top-5 추천
    ) {
        try {
            // 사용자 생일 가져오기
            Member member = memberRepository.findById(userId).orElseThrow(() ->
                    new RuntimeException("User not found"));
            LocalDate birthDay = member.getBirthDay();

            // 현재 날짜 기준 나이 계산
            int age = Period.between(birthDay, LocalDate.now()).getYears();

            // Flask 서버에 추천 요청
            RecommendationService.RecommendationResponseWrapper recommendations =
                    recommendationService.getRecommendations(userId, age, n);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("Error fetching recommendations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}