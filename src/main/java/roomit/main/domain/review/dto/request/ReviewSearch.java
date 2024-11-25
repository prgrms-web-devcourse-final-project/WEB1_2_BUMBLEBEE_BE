package roomit.main.domain.review.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearch {

    private Long lastId; // 마지막으로 조회된 reviewId

    @Builder.Default
    private Integer size = 10; // 가져올 데이터의 크기
}
