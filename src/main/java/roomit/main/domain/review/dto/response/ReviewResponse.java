package roomit.main.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import roomit.main.domain.review.entity.Review;

@Builder
public record ReviewResponse(
        String studyRoomName,
        String memberNickName,
        int reviewRating,
        String reviewContent,
        LocalDateTime reviewDate,
        Long reviewId)
{
    public ReviewResponse(Review review) {
        this(
                review.getReservation().getStudyRoom().getStudyRoomName().getValue(),
                review.getReservation().getMember().getMemberNickName(),
                review.getReviewRating(),
                review.getReviewContent(),
                LocalDateTime.now(),
                review.getReviewId()
        );
    }
}
