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
import roomit.main.domain.workplace.entity.value.WorkplaceName;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static roomit.main.domain.member.entity.QMember.member;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void register(ReviewRegisterRequest request, Long memberId) {

        Reservation reservation = reservationRepository.findById(request.reservatinId())
                .orElseThrow(ErrorCode.RESERVATIN_NOT_FOUND::commonException);
        // 본인이 예약한거지 확인하는거

        if (!Objects.equals(reservation.getMemberId().getMemberId(), memberId)){
            throw ErrorCode.REVIEW_UPDATE_FAIL.commonException();
        }

        Workplace workPlace = workplaceRepository.findByWorkplaceName(new WorkplaceName(request.workPlaceName()))
                    .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

            // 별점 총합 및 리뷰 개수 업데이트
        workPlace.changeStarSum(workPlace.getStarSum() + request.reviewRating());
        workPlace.changeReviewCount(workPlace.getReviewCount() + 1);

        reviewRepository.save(request.toEntity(reservation));
    }

    @Transactional
    public ReviewResponse update(Long reviewId, ReviewUpdateRequest request, String workPlaceName, Long memberId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        Reservation reservation = review.getReservation();

        Workplace workPlace = workplaceRepository.findByWorkplaceName(new WorkplaceName(workPlaceName))
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        boolean isTrue = review.checkMyReservation(reservation, memberId);
        if (isTrue){
            throw ErrorCode.REVIEW_UPDATE_FAIL.commonException();
        }

        // 기존 별점 제거 후 새로운 별점 추가
        workPlace.changeStarSum(workPlace.getStarSum() - review.getReviewRating()
                + request.reviewRating());

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

    public void remove(Long reviewId, String workPlaceName, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        Reservation reservation = review.getReservation();

        Workplace workPlace = workplaceRepository.findByWorkplaceName(new WorkplaceName(workPlaceName))
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        boolean isTrue = review.checkMyReservation(reservation, memberId);
        if (isTrue){
            throw ErrorCode.REVIEW_UPDATE_FAIL.commonException();
        }

        // 별점 총합 감소 및 리뷰 개수 감소
        workPlace.changeStarSum(workPlace.getStarSum() - review.getReviewRating());
        workPlace.changeReviewCount(workPlace.getReviewCount() - 1);

        workplaceRepository.save(workPlace);
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
