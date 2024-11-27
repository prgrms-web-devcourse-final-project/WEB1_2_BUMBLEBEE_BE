package roomit.main.domain.review.repository;

import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> getList(ReviewSearch reviewSearch, Long workId);
}
