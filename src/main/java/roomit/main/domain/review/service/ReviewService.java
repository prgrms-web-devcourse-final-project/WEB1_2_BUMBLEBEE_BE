package roomit.main.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
import roomit.main.domain.review.dto.response.ReviewResponse;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.review.repository.ReviewRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;

import static roomit.main.domain.member.entity.QMember.member;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ReservationRepository reservationRepository;
    public void register(ReviewRegisterRequest request) {


        Reservation reservation = reservationRepository.findById(request.reservatinId())
                .orElseThrow(ErrorCode.RESERVATIN_NOT_FOUND::commonException);

        Review review = Review.builder()
                .reviewContent(request.reviewContent())
                .reviewRating(request.reviewRating())
                .workplaceName(request.workPlaceName())
                .reservation(reservation)
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public ReviewResponse update(Long reviewId, ReviewUpdateRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        try {
            review.changeReviewContent(request.reviewContent());
            review.changeReviewRating(request.reviewRating());
        } catch (Exception e) {
            throw ErrorCode.REVIEW_UPDATE_EXCEPTION.commonException();
        }

        return new ReviewResponse(review);
    }

    public List<ReviewResponse> read(Long memberId) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        List<Reservation> reservations = member.getReservations();

        List<ReviewResponse> responses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            responses.add(new ReviewResponse(reservation.getReview()));
        }

        return responses;
    }

    public void remove(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getList(ReviewSearch reviewSearch, Long workId) {

        Workplace workplace = workplaceRepository.findById(workId)
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);
        return reviewRepository.getList(reviewSearch, workplace.getWorkplaceId())
                .stream()
                .map(ReviewResponse::new)
                .toList();
    }
}
