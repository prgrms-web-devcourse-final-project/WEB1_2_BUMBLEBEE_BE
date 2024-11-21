package roomit.web1_2_bumblebee_be.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.entity.QReview;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;

import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Review> getList(ReviewSearch reviewSearch) {
        return jpaQueryFactory.selectFrom(QReview.review)
                .limit(reviewSearch.getSize())
                .offset(reviewSearch.getOffset())
                .orderBy(QReview.review.reviewId.desc())
                .fetch();
    }
}
