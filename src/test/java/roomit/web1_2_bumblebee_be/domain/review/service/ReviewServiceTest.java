//package roomit.web1_2_bumblebee_be.domain.review.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
//import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
//import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
//import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewRegisterRequest;
//import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewSearch;
//import roomit.web1_2_bumblebee_be.domain.review.dto.request.ReviewUpdateRequest;
//import roomit.web1_2_bumblebee_be.domain.review.dto.response.ReviewResponse;
//import roomit.web1_2_bumblebee_be.domain.review.entity.Review;
//import roomit.web1_2_bumblebee_be.domain.review.repository.ReviewRepository;
//import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
//import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class ReviewServiceTest {
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @BeforeEach
//    void setUp() {
//        reviewRepository.deleteAll();
//        memberRepository.deleteAll();
//        workplaceRepository.deleteAll();
//    }
//
//    //    @Test
////    @DisplayName("리뷰 등록하기")
////    @Transactional
////    void test0() {
////        Member member = Member.builder()
////                .memberAge(Age.TEN)
////                .memberSex(Sex.FEMALE)
////                .memberRole(Role.ROLE_USER)
////                .memberPwd("1111")
////                .memberEmail("이시현@Naver.com")
////                .memberPhoneNumber("010-33230-23")
////                .memberNickName("치킨유저")
////                .build();
////
////        memberRepository.save(member);
////
////        Workplace workplace = Workplace.builder()
////                .workplaceName("사업장")
////                .workplacePhoneNumber("010-1234-1234")
////                .workplaceDescription("사업장 설명")
////                .workplaceAddress("대한민국")
////                .profileImage(null)
////                .imageType(null)
////                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
////                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
////                .build();
////        workplaceRepository.save(workplace);
////
////        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
////                .reviewContent("좋은 장소네요")
////                .reviewRating("별점2개")
////                .memberId(member.getMemberId())
////                .workplaceId(workplace.getWorkplaceId())
////                .build();
////
////        reviewService.register(request);
////
////        List<Review> all = reviewRepository.findAll();
////        assertEquals("좋은 장소네요", all.get(0).getReviewContent());
////
////    }
//    @Test
//    @DisplayName("리뷰 등록하기")
//    @Transactional
//    void test1() {
//        Member member = Member.builder()
//                .memberAge(Age.TEN)
//                .memberSex(Sex.FEMALE)
//                .memberRole(Role.ROLE_USER)
//                .memberPwd("1111")
//                .memberEmail("이시현@Naver.com")
//                .memberPhoneNumber("010-33230-23")
//                .memberNickName("치킨유저")
//                .build();
//
//        memberRepository.save(member);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//        workplaceRepository.save(workplace);
//
//        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
//                .reviewContent("좋은 장소네요")
//                .reviewRating("별점2개")
//                .memberId(member.getMemberId())
//                .workplaceId(workplace.getWorkplaceId())
//                .build();
//
//        reviewService.register(request);
//
//        List<Review> all = reviewRepository.findAll();
//        assertEquals("좋은 장소네요", all.get(0).getReviewContent());
//
//    }
//
//    @Test
//    @DisplayName("리뷰 수정하기")
//    @Transactional
//    void test2() {
//
//        Member member = Member.builder()
//                .memberAge(Age.TEN)
//                .memberSex(Sex.FEMALE)
//                .memberRole(Role.ROLE_USER)
//                .memberPwd("1111")
//                .memberEmail("이시현@Naver.com")
//                .memberPhoneNumber("010-33230-23")
//                .memberNickName("치킨유저")
//                .build();
//
//        memberRepository.save(member);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//        workplaceRepository.save(workplace);
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating("별점4개")
//                .member(member)
//                .workplace(workplace)
//                .build();
//
//        reviewRepository.save(review);
//
//        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
//                .reviewContent("치킨이 보이네요??")
//                .reviewRating("별점5개")
//                .workplaceId(workplace.getWorkplaceId())
//                .memberId(member.getMemberId())
//                .build();
//
//        reviewService.update(review.getReviewId(), reviewUpdateRequest);
//
//        List<Review> all = reviewRepository.findAll();
//        assertEquals("치킨이 보이네요??", all.get(0).getReviewContent());
//        assertEquals("별점5개", all.get(0).getReviewRating());
////        assertEquals(1L, all.get(0).getMember().getMemberId());
////        assertEquals(1L, all.get(0).getWorkplace().getWorkplaceId());
//
//    }
//
//    @Test
//    @DisplayName("리뷰 조회")
//    @Transactional
//    void test3() {
//
//        Member member = Member.builder()
//                .memberAge(Age.TEN)
//                .memberSex(Sex.FEMALE)
//                .memberRole(Role.ROLE_USER)
//                .memberPwd("1111")
//                .memberEmail("이시현@Naver.com")
//                .memberPhoneNumber("010-33230-23")
//                .memberNickName("치킨유저")
//                .build();
//
//        memberRepository.save(member);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//        workplaceRepository.save(workplace);
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating("별점4개")
//                .member(member)
//                .workplace(workplace)
//                .build();
//
//        reviewRepository.save(review);
//
//        reviewService.read(review.getReviewId());
//
//        List<Review> all = reviewRepository.findAll();
//        assertEquals("치킨이 안보이네요..", all.get(0).getReviewContent());
//        assertEquals("별점4개", all.get(0).getReviewRating());
//
//    }
//
//    @Test
//    @DisplayName("리뷰 조회")
//    @Transactional
//    void test4() {
//
//        Member member = Member.builder()
//                .memberAge(Age.TEN)
//                .memberSex(Sex.FEMALE)
//                .memberRole(Role.ROLE_USER)
//                .memberPwd("1111")
//                .memberEmail("이시현@Naver.com")
//                .memberPhoneNumber("010-33230-23")
//                .memberNickName("치킨유저")
//                .build();
//
//        memberRepository.save(member);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//        workplaceRepository.save(workplace);
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating("별점4개")
//                .member(member)
//                .workplace(workplace)
//                .build();
//
//        reviewRepository.save(review);
//
//        reviewService.remove(review.getReviewId());
//
//        assertEquals(0, reviewRepository.findAll().size());
//
//    }
//
//    @Test
//    @DisplayName("리뷰 페이징")
//    @Transactional
//    void test5() {
//
//        Member member = Member.builder()
//                .memberAge(Age.TEN)
//                .memberSex(Sex.FEMALE)
//                .memberRole(Role.ROLE_USER)
//                .memberPwd("1111")
//                .memberEmail("이시현@Naver.com")
//                .memberPhoneNumber("010-33230-23")
//                .memberNickName("치킨유저")
//                .build();
//
//        memberRepository.save(member);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//        workplaceRepository.save(workplace);
//
//        List<Review> list = IntStream.rangeClosed(0, 19).mapToObj(i -> Review.builder()
//                .reviewContent("치킨이 안보이네요.." + i)
//                .reviewRating("별점4개" + i)
//                .member(member)
//                .workplace(workplace)
//                .build()).toList();
//        ReviewSearch reviewSearch = ReviewSearch.builder()
//                .page(0)
//                .size(10)
//                .build();
//
//        reviewRepository.saveAll(list);
//
//        List<ReviewResponse> list1 = reviewService.getList(reviewSearch);
//
//        assertEquals(10, list1.size());
//        assertEquals("치킨이 안보이네요..19", list1.get(0).getReviewContent());
//
//
//
//    }
//}