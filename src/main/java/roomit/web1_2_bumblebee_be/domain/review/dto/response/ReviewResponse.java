package roomit.web1_2_bumblebee_be.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;


public record ReviewResponse(String workplaceName, String reviewRating, String reviewContent, LocalDateTime reviewDate,
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

    @Builder
    public ReviewResponse(String workplaceName, String reviewRating, String reviewContent, LocalDateTime reviewDate, Long reviewId) {
        this.workplaceName = workplaceName;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
    }

}
