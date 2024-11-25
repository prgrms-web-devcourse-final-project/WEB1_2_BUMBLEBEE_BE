
package roomit.web1_2_bumblebee_be.domain.review.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
import roomit.web1_2_bumblebee_be.domain.review.repository.ReviewRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private LocalDate date = LocalDate.of(2024, 11, 22);

    private Workplace workplace;

    private Member member;
    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        workplaceRepository.deleteAll();

        workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국sa")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        member = Member.builder()
                .birthDay(date)
                .memberSex(Sex.FEMALE)
                .memberRole(Role.ROLE_USER)
                .memberPwd("Bass!1123")
                .memberEmail("asd@naver.com")
                .memberPhoneNumber("010-3323-2323")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

    }
    @Test
    @DisplayName("리뷰 등록하기")
    @Transactional
    void test1() {

        memberRepository.save(member);

        workplaceRepository.save(workplace);

        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
                .reviewContent("좋은 장소네요")
                .reviewRating("별점2개")
                .memberId(member.getMemberId())
                .workplaceId(workplace.getWorkplaceId())
                .build();

        reviewService.register(request);

        List<Review> all = reviewRepository.findAll();
        assertEquals("좋은 장소네요", all.get(0).getReviewContent());

    }

    @Test
    @DisplayName("리뷰 수정하기")
    @Transactional
    void test2() {



        memberRepository.save(member);


        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);

        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
                .reviewContent("치킨이 보이네요??")
                .reviewRating("별점5개")
                .workplaceId(workplace.getWorkplaceId())
                .memberId(member.getMemberId())
                .build();

        reviewService.update(review.getReviewId(), reviewUpdateRequest);

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 보이네요??", all.get(0).getReviewContent());
        assertEquals("별점5개", all.get(0).getReviewRating());
//        assertEquals(1L, all.get(0).getMember().getMemberId());
//        assertEquals(1L, all.get(0).getWorkplace().getWorkplaceId());

    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    void test3() {



        memberRepository.save(member);


        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);

        reviewService.read(review.getReviewId());

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 안보이네요..", all.get(0).getReviewContent());
        assertEquals("별점4개", all.get(0).getReviewRating());

    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    void test4() {



        memberRepository.save(member);

        workplaceRepository.save(workplace);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating("별점4개")
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);

        reviewService.remove(review.getReviewId());

        assertEquals(0, reviewRepository.findAll().size());

    }

    @Test
    @DisplayName("리뷰 커서 기반 페이징")
    @Transactional
    void testCursorPagination() {

        // 1. 테스트 데이터 생성

        memberRepository.save(member);


        workplaceRepository.save(workplace);

        // 리뷰 데이터 20개 저장
        List<Review> reviews = IntStream.rangeClosed(1, 20).mapToObj(i -> Review.builder()
                .reviewContent("치킨이 안보이네요.." + i)
                .reviewRating("별점4개" + i)
                .member(member)
                .workplace(workplace)
                .build()).toList();

        reviewRepository.saveAll(reviews);

        // 2. 첫 번째 요청: 첫 10개의 리뷰 가져오기
        ReviewSearch firstPageSearch = ReviewSearch.builder()
                .lastId(null) // 첫 페이지이므로 lastId는 null
                .size(10)
                .build();

        List<ReviewResponse> firstPage = reviewService.getList(firstPageSearch);

        assertEquals(10, firstPage.size());
        assertEquals("치킨이 안보이네요..20", firstPage.get(0).reviewContent()); // 최신순 검증
        assertEquals("치킨이 안보이네요..11", firstPage.get(9).reviewContent()); // 마지막 항목 검증

        // 3. 두 번째 요청: 첫 페이지의 마지막 ID를 커서로 사용
        Long lastId = firstPage.get(firstPage.size() - 1).reviewId(); // 첫 페이지의 마지막 ID

        ReviewSearch secondPageSearch = ReviewSearch.builder()
                .lastId(lastId)
                .size(10)
                .build();

        List<ReviewResponse> secondPage = reviewService.getList(secondPageSearch);

        assertEquals(10, secondPage.size());
        assertEquals("치킨이 안보이네요..10", secondPage.get(0).reviewContent()); // 다음 페이지 첫 항목 검증
        assertEquals("치킨이 안보이네요..1", secondPage.get(9).reviewContent()); // 다음 페이지 마지막 항목 검증

        // 4. 끝 페이지 요청: 두 번째 페이지의 마지막 ID를 커서로 사용
        lastId = secondPage.get(secondPage.size() - 1).reviewId();

        ReviewSearch lastPageSearch = ReviewSearch.builder()
                .lastId(lastId)
                .size(10)
                .build();

        List<ReviewResponse> lastPage = reviewService.getList(lastPageSearch);

        assertTrue(lastPage.isEmpty()); // 데이터가 더 이상 없음을 확인
    }
}

