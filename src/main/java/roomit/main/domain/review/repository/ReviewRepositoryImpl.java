package roomit.main.domain.review.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import roomit.main.domain.reservation.entity.QReservation;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.entity.QReview;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.studyroom.entity.QStudyRoom;
import roomit.main.domain.workplace.entity.QWorkplace;

import java.util.Collections;
import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }




    @Override
    public List<Review> getList(ReviewSearch reviewSearch, Long workId) {
        QReview review = QReview.review;
        QReservation reservation = QReservation.reservation;
        QStudyRoom studyRoom = QStudyRoom.studyRoom;
        QWorkplace workplace = QWorkplace.workplace;

        // lastId가 null이 아니면 해당 ID가 존재하는지 확인하는 로직
        if (reviewSearch.getLastId() != null) {
            boolean exists = jpaQueryFactory.selectOne()
                    .from(review)
                    .where(review.reviewId.eq(reviewSearch.getLastId()))
                    .fetchFirst() != null;

            if (!exists) {
                return Collections.emptyList(); // lastId가 유효하지 않으면 빈 리스트 반환
            }
        }

        // cursorCondition: lastId가 있을 경우 해당 ID보다 작은 reviewId를 가져옴
        BooleanExpression cursorCondition = reviewSearch.getLastId() == null
                ? null
                : review.reviewId.lt(reviewSearch.getLastId());

        // workId에 해당하는 workplace 조건
        BooleanExpression workplaceCondition = workplace.workplaceId.eq(workId);

        // 최종 조건: cursorCondition이 있으면 그것과 workplaceCondition을 결합, 없으면 workplaceCondition만 적용
        BooleanExpression finalCondition = cursorCondition != null
                ? cursorCondition.and(workplaceCondition)
                : workplaceCondition;

        // 리뷰를 조회하는 쿼리
        return jpaQueryFactory.selectFrom(review)
                .join(review.reservation, reservation) // 예약 정보와 조인
                .join(reservation.studyRoomId, studyRoom) // 학습룸 정보와 조인
                .join(studyRoom.workPlaceId, workplace) // 직장 정보와 조인
                .where(finalCondition) // 조건 적용
                .limit(reviewSearch.getSize()) // 페이지 크기 설정
                .orderBy(review.reviewId.desc()) // 최신순 정렬
                .fetch(); // 결과 리스트 반환
    }
}
