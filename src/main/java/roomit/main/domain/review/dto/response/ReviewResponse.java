package roomit.main.domain.review.dto.response;

import lombok.Builder;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;


public record ReviewResponse(String workplaceName, Double reviewRating, String reviewContent, LocalDateTime reviewDate,
                             Long reviewId) {


    public ReviewResponse(Review review) {
        this(
                review.getWorkplace().getWorkplaceName().getValue(),
                review.getReviewRating(),
                review.getReviewContent(),
                LocalDateTime.now(),
                review.getReviewId()
        );
    }

    public ReviewResponse(Review review, Workplace workplace) {
        this(
                workplace.getWorkplaceName().getValue(),
                review.getReviewRating(),
                review.getReviewContent(),
                LocalDateTime.now(),
                review.getReviewId()
        );
    }

    @Builder
    public ReviewResponse(String workplaceName, Double reviewRating, String reviewContent, LocalDateTime reviewDate, Long reviewId) {
        this.workplaceName = workplaceName;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
    }

}
