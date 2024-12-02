package roomit.main.domain.review.dto.response;

import lombok.Builder;
import roomit.main.domain.review.entity.Review;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse( String studyRoomName, int reviewRating, String reviewContent, LocalDateTime reviewDate,
                             Long reviewId) {


    public ReviewResponse(Review review) {
        this(
                review.getReservation().getStudyRoomId().getStudyRoomName().getValue(),
                review.getReviewRating(),
                review.getReviewContent(),
                LocalDateTime.now(),
                review.getReviewId()
        );
    }
}
