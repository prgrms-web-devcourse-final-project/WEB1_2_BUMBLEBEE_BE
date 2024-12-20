package roomit.main.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.review.entity.Review;

@Builder
public record ReviewRegisterRequest(@NotNull(message = "예약 id는 필수입니다.") Long reservationId,
                                    @NotBlank(message = "사업장 이름") String workPlaceName,
                                    @NotBlank(message = "리뷰 내용을 적어주세요.") String reviewContent,
                                    @NotNull(message = "별점 입력해 주세요.") int reviewRating) {


    public Review toEntity(Reservation reservation){
        return  Review.builder()
                .reviewContent(reviewContent)
                .reviewRating(reviewRating)
                .workplaceName(workPlaceName)
                .reservation(reservation)
                .build();
    }
}
