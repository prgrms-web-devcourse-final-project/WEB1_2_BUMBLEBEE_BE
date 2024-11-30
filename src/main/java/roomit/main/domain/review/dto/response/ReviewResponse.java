package roomit.main.domain.review.dto.response;

import lombok.Builder;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.workplace.entity.Workplace;

import java.time.LocalDateTime;


public record ReviewResponse( String studyRoomName, Double reviewRating, String reviewContent, LocalDateTime reviewDate,
                             Long reviewId) {


    public ReviewResponse(Review review) {
        this(
                review.getReservation().getStudyRoomId().getTitle(),
                review.getReviewRating(),
                review.getReviewContent(),
                LocalDateTime.now(),
                review.getReviewId()
        );
    }

    @Builder
    public ReviewResponse(String studyRoomName, Double reviewRating, String reviewContent, LocalDateTime reviewDate, Long reviewId) {
        this.studyRoomName = studyRoomName;
        this.reviewRating = reviewRating;
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.reviewId = reviewId;
    }

}
