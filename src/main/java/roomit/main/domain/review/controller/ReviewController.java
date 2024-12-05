package roomit.main.domain.review.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
import roomit.main.domain.review.dto.response.CursorResponse;
import roomit.main.domain.review.dto.response.ReviewMeResponse;
import roomit.main.domain.review.dto.response.ReviewResponse;
import roomit.main.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@RequestBody @Valid ReviewRegisterRequest request, @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        reviewService.register(request, memberDetails.getId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update/{reviewId}")
    public ReviewResponse update(
            @RequestBody @Valid ReviewUpdateRequest request
            , @PathVariable Long reviewId
            , @RequestParam String workplaceName
            , @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        return reviewService.update(reviewId, request, workplaceName, memberDetails.getId());
    }

    // 단건 리뷰 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public List<ReviewMeResponse> read(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

        return reviewService.read(customMemberDetails.getId());
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/workplace/{workplaceId}")
    public CursorResponse getReviews(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long workplaceId) {

        ReviewSearch reviewSearch = ReviewSearch.builder()
                .lastId(lastId) // 커서를 사용
                .size(size)
                .build();

        List<ReviewResponse> reviews = reviewService.getList(reviewSearch, workplaceId);

        Long nextCursor = reviews.isEmpty()
                ? null
                : reviews.get(reviews.size() - 1).reviewId();

        return CursorResponse
                .builder()
                .data(reviews)
                .nextCursor(nextCursor)
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{reviewId}")
    public void remove(@PathVariable Long reviewId,
                       @RequestParam String workplaceName,
                       @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        reviewService.remove(reviewId, workplaceName, memberDetails.getId());
    }
}
