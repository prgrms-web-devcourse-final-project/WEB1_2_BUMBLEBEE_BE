package roomit.web1_2_bumblebee_be.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> , ReviewRepositoryCustom{
}
