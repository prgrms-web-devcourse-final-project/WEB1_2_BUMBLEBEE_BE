package roomit.main.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;


public record ReviewUpdateRequest(
                                  @NotNull(message = "리뷰를 적을 사업장 번호를 적어주세요") Long workplaceId,
                                  @NotBlank(message = "리뷰 내용을 적어주세요.") String reviewContent,
                                  @NotNull(message = "별점 입력해 주세요.") Double reviewRating,
                                  LocalDateTime reviewUpdateTime) {
    @Builder
    public ReviewUpdateRequest  {
    }
}
