package roomit.web1_2_bumblebee_be.domain.review.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.CursorResponse;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/api/v1/review/register")
    public ResponseEntity<?> register(@RequestBody @Valid ReviewRegisterRequest request) {
        reviewService.register(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/api/v1/review/update/{reviewId}")
    public ResponseEntity<ReviewResponse> update(@RequestBody @Valid ReviewUpdateRequest request
    ,@PathVariable Long reviewId) {
         return ResponseEntity.ok(reviewService.update(reviewId, request));
    }

    // 단건 리뷰 조회
    @GetMapping("/api/v1/review/{reviewId}")
    public ResponseEntity<ReviewResponse> read(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.read(reviewId));
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
                : reviews.get(reviews.size() - 1).getReviewId();

        return ResponseEntity.ok(CursorResponse
                .builder()
                .data(reviews)
                .nextCursor(nextCursor)
                .build());
    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    public ResponseEntity<?> remove(@PathVariable Long reviewId) {
        reviewService.remove(reviewId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
