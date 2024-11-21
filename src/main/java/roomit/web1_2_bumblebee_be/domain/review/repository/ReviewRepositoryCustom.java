package roomit.web1_2_bumblebee_be.domain.review.repository;

import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> getList(ReviewSearch reviewSearch);
}
