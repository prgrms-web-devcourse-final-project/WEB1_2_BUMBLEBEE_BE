package roomit.web1_2_bumblebee_be.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.review.repository.ReviewRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WorkplaceRepository workplaceRepository;

    public void register(ReviewRegisterRequest request) {

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        Workplace workplace = workplaceRepository.findById(request.workplaceId())
                .orElseThrow(ErrorCode.WORKPLACE_NOT_FOUND::commonException);

        Review review = Review.builder()
                .reviewContent(request.reviewContent())
                .reviewRating(request.reviewRating())
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);
    }

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

    public ReviewResponse read(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        return new ReviewResponse(review);
    }

    public void remove(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ErrorCode.REVIEW_NOT_FOUND::commonException);

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getList(ReviewSearch reviewSearch) {

        return reviewRepository.getList(reviewSearch)
                .stream()
                .map(ReviewResponse::new)
                .toList();
    }
}
