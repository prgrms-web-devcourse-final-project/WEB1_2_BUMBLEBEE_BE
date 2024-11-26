package roomit.main.domain.review.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Age;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Role;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
import roomit.main.domain.review.dto.response.ReviewResponse;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.review.repository.ReviewRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private LocalDate date;

    private Member member;

    private Workplace workplace;
    @BeforeAll
    void setUp() {

        date = LocalDate.of(2024, 11, 22);

        member =  Member.builder()
                .birthDay(date)
                .memberSex(Sex.FEMALE)
                .memberPwd("Business1!")
                .memberEmail("wfqdfqwf@naver.com")
                .memberPhoneNumber("010-1323-2154")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

         workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국입니다")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();
        memberRepository.save(member);

        workplaceRepository.save(workplace);
    }
    @Test
    @DisplayName("리뷰 등록하기")
    @Transactional
    void test1() {



        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
                .reviewContent("좋은 장소네요")
                .reviewRating(3.5)
                .workplaceId(workplace.getWorkplaceId())
                .build();

        reviewService.register(request, customMemberDetails.getId());

        List<Review> all = reviewRepository.findAll();
        assertEquals("좋은 장소네요", all.get(0).getReviewContent());

    }

    @Test
    @DisplayName("리뷰 수정하기")
    @Transactional
    void test2() {



        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating(4.2)
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);

        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
                .reviewContent("치킨이 보이네요??")
                .reviewRating(4.1)
                .workplaceId(workplace.getWorkplaceId())
                .memberId(member.getMemberId())
                .build();

        reviewService.update(review.getReviewId(), reviewUpdateRequest);

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 보이네요??", all.get(0).getReviewContent());
        assertEquals(4.1, all.get(0).getReviewRating());
//        assertEquals(1L, all.get(0).getMember().getMemberId());
//        assertEquals(1L, all.get(0).getWorkplace().getWorkplaceId());

    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    void test3() {



        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating(1.3)
                .member(member)
                .workplace(workplace)
                .build();


        reviewRepository.save(review);

        reviewService.read(review.getReviewId());

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 안보이네요..", all.get(0).getReviewContent());
        assertEquals(1.3, all.get(0).getReviewRating());

    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    void test4() {



        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating(1.9)
                .member(member)
                .workplace(workplace)
                .build();

        reviewRepository.save(review);

        reviewService.remove(review.getReviewId());

        assertEquals(0, reviewRepository.findAll().size());

    }

    @Test
    @DisplayName("리뷰 페이징")
    @Transactional
    void test5() {


        // 리뷰 데이터 20개 저장
        List<Review> reviews = IntStream.rangeClosed(1, 20).mapToObj(i -> Review.builder()
                .reviewContent("치킨이 안보이네요.." + i)
                .reviewRating(3.1 + i)
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

        assertEquals(10, secondPage.size());// 다음 페이지 첫 항목 검증
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