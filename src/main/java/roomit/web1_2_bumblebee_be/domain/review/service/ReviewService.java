package roomit.web1_2_bumblebee_be.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.exception.MemberNotFound;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.review.exception.ReviewNotFound;
import roomit.web1_2_bumblebee_be.domain.review.exception.ReviewUpdateException;
import roomit.web1_2_bumblebee_be.domain.review.repository.ReviewRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotFound;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final WorkplaceRepository workplaceRepository;

    public void register(ReviewRegisterRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFound::new);

        Workplace workplace = workplaceRepository.findById(request.getWorkplaceId())
                .orElseThrow(WorkplaceNotFound::new);

        Review review = Review.builder()
                .reviewContent(request.getReviewContent())
                .reviewRating(request.getReviewRating())
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);
    }

    public ReviewResponse update(Long reviewId, ReviewUpdateRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFound::new);
        try {
            review.changeReviewContent(request.getReviewContent());
            review.changeReviewRating(request.getReviewRating());
        }catch (Exception e) {
            throw new ReviewUpdateException();
        }

        return new ReviewResponse(review);
    }

    public ReviewResponse read(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFound::new);

        return new ReviewResponse(review);
    }

    public void remove(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFound::new);

        reviewRepository.delete(review);
    }
}
