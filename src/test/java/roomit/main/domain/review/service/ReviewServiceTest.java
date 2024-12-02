package roomit.main.domain.review.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.reservation.entity.Reservation;
import roomit.main.domain.reservation.entity.ReservationState;
import roomit.main.domain.reservation.repository.ReservationRepository;
import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
import roomit.main.domain.review.dto.request.ReviewSearch;
import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
import roomit.main.domain.review.dto.response.ReviewResponse;
import roomit.main.domain.review.entity.Review;
import roomit.main.domain.review.repository.ReviewRepository;
import roomit.main.domain.studyroom.entity.StudyRoom;
import roomit.main.domain.studyroom.repository.StudyRoomRepository;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import roomit.main.global.service.ImageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private ReservationRepository reservationRepository;

    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ImageService imageService;

    private LocalDate date;

    private Member member;

    private Reservation reservation;

    private Workplace workplace;

    private StudyRoom studyRoom;

    private CustomMemberDetails customMemberDetails;

    @BeforeAll
    void setUp() {

        date = LocalDate.of(2024, 11, 22);


        workplace = Workplace.builder()
                .workplaceName("Workplace")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
                .imageUrl(imageService.createImageUrl("Workplace"))
                .workplaceStartTime(LocalTime.of(9, 0))
                .workplaceEndTime(LocalTime.of(18, 0))
                .business(null)
                .build();
        workplaceRepository.save(workplace);

        member =  Member.builder()
                .birthDay(date)
                .memberSex(Sex.FEMALE)
                .memberPwd("Business1!")
                .memberEmail("wfqdfqwf@naver.com")
                .memberPhoneNumber("010-1323-2154")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

        memberRepository.save(member);

        studyRoom = StudyRoom.builder()
                .studyRoomName("Test Room")
                .description("A test room")
                .capacity(10)
                .price(100)
                .imageUrl(imageService.createImageUrl("Workplace/Test Room"))
                .workplace(workplace)
                .build();

        studyRoomRepository.save(studyRoom);


        reservation = Reservation.builder()
                .reservationName("이시현")
                .reservationPhoneNumber("010-2314-2512")
                .reservationState(ReservationState.RESERVABLE)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .studyRoomId(studyRoom)
                .memberId(member)
                .reservationCapacity(123)
                .reservationPrice(1000)
                .build();

         customMemberDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    @Test
    @DisplayName("리뷰 등록하기")
    @Transactional
    void test1() {
        reservationRepository.save(reservation);

        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
                .reviewContent("좋은 장소네요")
                .reviewRating(3)
                .reservatinId(reservation.getId())
                .workPlaceName(workplace.getWorkplaceName().getValue())
                .build();



        reviewService.register(request, customMemberDetails.getId());
        Workplace workplace1 = workplaceRepository.findByWorkplaceName(workplace.getWorkplaceName()).get();

        assertEquals(3, workplace1.getStarSum());

        List<Review> all = reviewRepository.findAll();
        System.out.println(Arrays.toString(all.toArray()));
        assertEquals("좋은 장소네요", all.get(0).getReviewContent());

    }

    @Test
    @DisplayName("리뷰 수정하기")
    @Transactional
    void test2() {
        Reservation reservation1 = reservationRepository.save(reservation);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating(1)
                .workplaceName(workplace.getWorkplaceName().getValue())
                .reservation(reservation1)
                .build();
        reviewRepository.save(review);

        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
                .reviewContent("치킨이 보이네요??")
                .reviewRating(4)
                .build();

        reviewService.update(review.getReviewId(), reviewUpdateRequest, workplace.getWorkplaceName().getValue(), customMemberDetails.getId());

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 보이네요??", all.get(0).getReviewContent());
        assertEquals(4, all.get(0).getReviewRating());
        assertEquals("Test Room",all.get(0).getReservation().getStudyRoomId().getStudyRoomName().getValue());
//        assertEquals(1L, all.get(0).getMember().getMemberId());
//        assertEquals(1L, all.get(0).getWorkplace().getWorkplaceId());

    }

    @Test
    @DisplayName("리뷰 조회")
    @Transactional
    void test3() {


        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        IntStream.rangeClosed(1, 20).forEach(i -> {


            Reservation reservation = Reservation.builder()
                    .reservationName("이시현")
                    .reservationPhoneNumber("010-2314-2512")
                    .reservationState(ReservationState.RESERVABLE)
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .reservationPrice(1000)
                    .reservationCapacity(100)
                    .studyRoomId(studyRoom)
                    .memberId(member)
                    .build();

            Reservation reservation1 = reservationRepository.save(reservation);

            Review review1 = Review.builder()
                    .reviewContent("치킨이 안보이네요.." + i)
                    .reviewRating(3 + i)
                    .reservation(reservation1)
                    .workplaceName(workplace.getWorkplaceName().getValue())
                    .build();
            reviewRepository.save(review1);

            reservation1.addReview(review1);
        });

        reviewService.read(customMemberDetails.getId());

        List<Review> all = reviewRepository.findAll();
        assertEquals("치킨이 안보이네요..1", all.get(0).getReviewContent());
        assertEquals(20, all.size());
    }

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    void test4() {

        workplaceRepository.save(workplace);

        Reservation reservation1 = reservationRepository.save(reservation);

        Review review = Review.builder()
                .reviewContent("치킨이 안보이네요..")
                .reviewRating(1)
                .reservation(reservation1)
                .workplaceName(workplace.getWorkplaceName().getValue())
                .build();

        reviewRepository.save(review);

        reviewService.remove(review.getReviewId(), workplace.getWorkplaceName().getValue(), customMemberDetails.getId());
        assertEquals(0, reviewRepository.findAll().size());

    }

    @Transactional
    @DisplayName("커서 기반 페이징")
    @Test
    void test6() {

        IntStream.rangeClosed(1, 20).forEach(i -> {


            Reservation reservation = Reservation.builder()
                    .reservationName("이시현")
                    .reservationPhoneNumber("010-2314-2512")
                    .reservationState(ReservationState.RESERVABLE)
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now())
                    .studyRoomId(studyRoom)
                    .reservationCapacity(10)
                    .reservationPrice(1000)
                    .memberId(member)
                    .build();

            Reservation reservation1 = reservationRepository.save(reservation);

            Review review1 = Review.builder()
                    .reviewContent("치킨이 안보이네요.." + i)
                    .reviewRating(3 + i)
                    .reservation(reservation1)
                    .workplaceName(workplace.getWorkplaceName().getValue())
                    .build();
            reviewRepository.save(review1);

            reservation1.addReview(review1);
        });

        // 첫 번째 요청: 첫 10개의 리뷰 가져오기
        ReviewSearch firstPageSearch = ReviewSearch.builder()
                .lastId(null) // 첫 페이지이므로 lastId는 null
                .size(10)
                .build();
        Long workplaceId = workplace.getWorkplaceId();
        List<ReviewResponse> firstPage = reviewService.getList(firstPageSearch, workplaceId);

        assertEquals(10, firstPage.size());
        assertEquals("치킨이 안보이네요..20", firstPage.get(0).reviewContent()); // 최신순 검증
        assertEquals("치킨이 안보이네요..11", firstPage.get(9).reviewContent()); // 마지막 항목 검증

        // 두 번째 요청: 첫 페이지의 마지막 ID를 커서로 사용
        Long lastId = firstPage.get(firstPage.size() - 1).reviewId(); // 첫 페이지의 마지막 ID
        ReviewSearch secondPageSearch = ReviewSearch.builder()
                .lastId(lastId)
                .size(10)
                .build();

        List<ReviewResponse> secondPage = reviewService.getList(secondPageSearch, workplaceId);

        assertEquals(10, secondPage.size()); // 다음 페이지 첫 항목 검증
        assertEquals("치킨이 안보이네요..1", secondPage.get(9).reviewContent()); // 다음 페이지 마지막 항목 검증

        // 끝 페이지 요청: 두 번째 페이지의 마지막 ID를 커서로 사용
        lastId = secondPage.get(secondPage.size() - 1).reviewId();
        ReviewSearch lastPageSearch = ReviewSearch.builder()
                .lastId(lastId)
                .size(10)
                .build();

        List<ReviewResponse> lastPage = reviewService.getList(lastPageSearch, workplaceId);
        assertTrue(lastPage.isEmpty()); // 데이터가 더 이상 없음을 확인
    }
}