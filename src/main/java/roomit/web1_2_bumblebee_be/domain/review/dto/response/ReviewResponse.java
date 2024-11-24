package roomit.web1_2_bumblebee_be.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private final String workplaceName;
    private final String reviewRating;
    private final String reviewContent;
    private final LocalDateTime reviewDate;
    private final Long reviewId;

    public ReviewResponse(Review review) {
        this.workplaceName = review.getWorkplace().getWorkplaceName().getValue();
        this.reviewRating = review.getReviewRating();
        this.reviewContent = review.getReviewContent();
        this.reviewId = review.getReviewId();
        this.reviewDate = LocalDateTime.now();
    }

    @Builder
    public ReviewResponse(String workplaceName, String reviewRating, String reviewContent, LocalDateTime reviewDate, Long reviewId) {
        this.workplaceName = workplaceName;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
    }

}
