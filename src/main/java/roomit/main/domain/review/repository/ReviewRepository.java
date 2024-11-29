package roomit.main.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomit.main.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> , ReviewRepositoryCustom{
}
