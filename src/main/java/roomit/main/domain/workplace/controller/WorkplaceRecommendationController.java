package roomit.main.domain.workplace.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.workplace.dto.response.WorkplaceRecommendResponse;
import roomit.main.domain.workplace.service.WorkplaceRecommendationService;
import roomit.main.global.error.ErrorCode;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/recommend")
public class WorkplaceRecommendationController {

    private final WorkplaceRecommendationService recommendationService;
    private final MemberRepository memberRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkplaceRecommendResponse> getRecommendations(@AuthenticationPrincipal CustomMemberDetails customMemberDetails,
                                                               @RequestParam(defaultValue = "5") Integer n) {
        try {
            // 사용자 생일 가져오기
            Member member = memberRepository.findById(customMemberDetails.getId()).orElseThrow(() ->
                    new RuntimeException("User not found"));
            LocalDate birthDay = member.getBirthDay();

            // 현재 날짜 기준 나이 계산
            int age = Period.between(birthDay, LocalDate.now()).getYears();

            return recommendationService.getRecommendations(customMemberDetails.getId(), age, n);
        } catch (Exception e) {
            throw ErrorCode.WORKPLACE_RECOMMEND_FAIL.commonException();
        }
    }
}