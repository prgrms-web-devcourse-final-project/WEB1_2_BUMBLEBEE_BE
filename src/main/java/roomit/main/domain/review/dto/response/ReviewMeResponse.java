package roomit.main.domain.review.dto.response;

import lombok.Builder;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.global.service.FileLocationService;

import java.time.LocalDateTime;

@Builder
public record ReviewMeResponse(
        String studyRoomName,
        int reviewRating,
        String reviewContent,
        LocalDateTime reviewDate,
        String workplaceName,
        String workplaceImageURL,
        Long workplaceId,
        Long reviewId)
{
    public ReviewMeResponse(Review review, Workplace workplace, FileLocationService fileLocationService) {
        this(
                review.getReservation().getStudyRoom().getStudyRoomName().getValue(),
                review.getReviewRating(),
                review.getReviewContent(),
                review.getCreatedAt(),
                review.getWorkplaceName(),
                fileLocationService.getImagesFromFolder(workplace.getImageUrl().getValue()).get(0),
                workplace.getWorkplaceId(),
                review.getReviewId()
        );
    }
}
