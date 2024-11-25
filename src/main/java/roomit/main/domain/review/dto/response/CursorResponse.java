package roomit.main.domain.review.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * @param data 조회된 데이터
 */
public record CursorResponse(List data, Long nextCursor) {
    @Builder
    public CursorResponse { // 다음 요청을 위한 커서 (마지막 reviewId)
    }
}