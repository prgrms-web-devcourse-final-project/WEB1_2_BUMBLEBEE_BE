package roomit.web1_2_bumblebee_be.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CursorResponse {
    private List data; // 조회된 데이터
    private Long nextCursor;

    @Builder
    public CursorResponse(List data, Long nextCursor) { // 다음 요청을 위한 커서 (마지막 reviewId)
        this.data = data;
        this.nextCursor = nextCursor;
    }
}