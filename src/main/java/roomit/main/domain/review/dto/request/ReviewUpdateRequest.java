package roomit.main.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewUpdateRequest(
                                  @NotBlank(message = "리뷰 내용을 적어주세요.") String reviewContent,
                                  @NotNull(message = "별점 입력해 주세요.") int reviewRating,
                                  LocalDateTime reviewUpdateTime) {
}
