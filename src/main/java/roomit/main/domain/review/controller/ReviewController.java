package roomit.main.domain.review.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
import roomit.main.domain.review.dto.response.CursorResponse;
import roomit.main.domain.review.dto.response.ReviewResponse;
import roomit.main.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/review/register")
    public void register(@RequestBody @Valid ReviewRegisterRequest request) {
        reviewService.register(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/v1/review/update/{reviewId}")
    public ReviewResponse update(@RequestBody @Valid ReviewUpdateRequest request
    ,@PathVariable Long reviewId) {
         return reviewService.update(reviewId, request);
    }

    // 단건 리뷰 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/review/{reviewId}")
    public ReviewResponse read(@PathVariable Long reviewId) {
       return reviewService.read(reviewId);
    }

    // 리뷰 페이징
    @GetMapping("/api/v1/review")
    public ResponseEntity<CursorResponse> getReviews(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        ReviewSearch reviewSearch = ReviewSearch.builder()
                .lastId(lastId) // 커서를 사용
                .size(size)
                .build();

        List<ReviewResponse> reviews = reviewService.getList(reviewSearch);

        Long nextCursor = reviews.isEmpty()
                ? null
                : reviews.get(reviews.size() - 1).reviewId();

        return ResponseEntity.ok(CursorResponse
                .builder()
                .data(reviews)
                .nextCursor(nextCursor)
                .build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/review/{reviewId}")
    public void remove(@PathVariable Long reviewId) {
        reviewService.remove(reviewId);
    }
}
