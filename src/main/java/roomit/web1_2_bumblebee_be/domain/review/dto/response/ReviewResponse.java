package roomit.web1_2_bumblebee_be.domain.review.dto.response;

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

    public ReviewResponse(Review review) {
        this.workplaceName = review.getWorkplace().getWorkplaceName();
        this.reviewRating = review.getReviewRating();
        this.reviewContent = review.getReviewContent();
        this.reviewDate = LocalDateTime.now();
    }
}
