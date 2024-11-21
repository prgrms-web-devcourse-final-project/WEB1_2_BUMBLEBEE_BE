package roomit.web1_2_bumblebee_be.domain.review.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
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
        return ResponseEntity.ok("리뷰등록 완료");
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
    public ResponseEntity<List<ReviewResponse>> readAll(ReviewSearch reviewSearch) {
        return ResponseEntity.ok(reviewService.getList(reviewSearch));

    }

    @DeleteMapping("/api/v1/review/{reviewId}")
    public ResponseEntity<?> remove(@PathVariable Long reviewId) {
        reviewService.remove(reviewId);
        return ResponseEntity.ok("리뷰 삭제 성공");
    }
}
