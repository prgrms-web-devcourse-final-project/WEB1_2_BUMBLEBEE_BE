//package roomit.main.domain.review.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jayway.jsonpath.JsonPath;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.transaction.annotation.Transactional;
//import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.business.service.BusinessService;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.reservation.entity.Reservation;
//import roomit.main.domain.reservation.entity.ReservationState;
//import roomit.main.domain.reservation.repository.ReservationRepository;
//import roomit.main.domain.review.dto.request.ReviewRegisterRequest;
//import roomit.main.domain.review.dto.request.ReviewUpdateRequest;
//import roomit.main.domain.review.entity.Review;
//import roomit.main.domain.review.repository.ReviewRepository;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.service.ImageService;
//import roomit.main.global.token.dto.request.LoginRequest;
//@AutoConfigureMockMvc
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//@Transactional
//class ReviewControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private StudyRoomRepository studyRoomRepository;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private BusinessService businessService;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    private LocalDate date;
//
//    private Member member;
//
//    private Workplace workplace;
//
//    private String token;
//
//    private Reservation reservation;
//
//    private StudyRoom studyRoom;
//
//    @BeforeAll
//    void setUp() throws Exception {
//
//        date = LocalDate.of(2024, 11, 22);
//
//        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                .businessName("테스트")
//                .businessEmail("business123@gmail.com")
//                .businessPwd("Business1!")
//                .businessNum("146-12-45639")
//                .build();
//
//        businessService.signUpBusiness(businessRegisterRequest); // 데이터 생성
//
//        Business business = businessRepository.findByBusinessEmail("business123@gmail.com")
//                .orElseThrow();
//
//        workplace = Workplace.builder()
//                .workplaceName("사업장 넘버원")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장 넘버원"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//        workplaceRepository.save(workplace);
//
//        member =  Member.builder()
//                .birthDay(date)
//                .memberSex(Sex.FEMALE)
//                .memberPwd("Business1!")
//                .memberEmail("qwdfasdf@naver.com")
//                .memberPhoneNumber("010-1323-2154")
//                .memberNickName("치킨유저")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();
//
//        memberRepository.save(member);
//
//        studyRoom = StudyRoom.builder()
//                .studyRoomName("Test Room")
//                .description("A test room")
//                .capacity(10)
//                .price(100)
//                .imageUrl(imageService.createImageUrl("사업장 넘버원/test Room"))
//                .workplace(workplace)
//                .build();
//
//        studyRoomRepository.save(studyRoom);
//
//
//        reservation = Reservation.builder()
//                .reservationName("이시현")
//                .reservationPhoneNumber("010-2314-2512")
//                .reservationState(ReservationState.COMPLETED)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now())
//                .studyRoom(studyRoom)
//                .reservationCapacity(10)
//                .reservationPrice(1000)
//                .member(member)
//                .build();
//
//
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("qwdfasdf@naver.com")
//                .password("Business1!")
//                .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        token = loginResult.getResponse().getHeader("Authorization");
//
//    }
//
//    @Test
//    @DisplayName("리뷰 등록")
//    @Transactional
//    void test1() throws Exception{
//
//        reservationRepository.save(reservation);
//
//        ReviewRegisterRequest request = ReviewRegisterRequest.builder()
//                .reviewContent("좋은 장소네요")
//                .reviewRating(3)
//                .reservationId(reservation.getReservationId())
//                .workPlaceName(workplace.getWorkplaceName().getValue())
//                .build();
//
//        mockMvc.perform(post("/api/v1/review/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 수정")
//    void test2() throws Exception{
//
//        Reservation reservation1 = Reservation.builder()
//                .reservationName("이시현")
//                .reservationPhoneNumber("010-2314-2512")
//                .reservationState(ReservationState.COMPLETED)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now())
//                .reservationPrice(1000)
//                .reservationCapacity(10)
//                .studyRoom(studyRoom)
//                .member(member)
//                .build();
//
//        Reservation save = reservationRepository.save(reservation1);
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating(1)
//                .reservation(save)
//                .workplaceName(workplace.getWorkplaceName().getValue())
//                .build();
//        reviewRepository.save(review);
//
//
//        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
//                .reviewContent("좋은 장소네요")
//                .reviewRating(2)
//                .build();
//
//        mockMvc.perform(put("/api/v1/review/update/{reviewId}?workplaceName=사업장 넘버원",review.getReviewId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 조회")
//    void test3() throws Exception{
//
//
//
//        IntStream.rangeClosed(1, 20).forEach(i -> {
//
//
//            Reservation reservation = Reservation.builder()
//                    .reservationName("이시현")
//                    .reservationPhoneNumber("010-2314-2512")
//                    .reservationState(ReservationState.COMPLETED)
//                    .startTime(LocalDateTime.now())
//                    .endTime(LocalDateTime.now())
//                    .reservationPrice(1000)
//                    .reservationCapacity(10)
//                    .studyRoom(studyRoom)
//                    .member(member)
//                    .build();
//
//            Reservation reservation1 = reservationRepository.save(reservation);
//
//            Review review1 = Review.builder()
//                    .reviewContent("치킨이 안보이네요.." + i)
//                    .reviewRating(3 + i)
//                    .reservation(reservation1)
//                    .workplaceName(workplace.getWorkplaceName().getValue())
//                    .build();
//
//            reviewRepository.save(review1);
//
//            reservation1.addReview(review1);
//        });
//
//        mockMvc.perform(get("/api/v1/review/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("리뷰 삭제")
//    void test4() throws Exception{
//
//        Reservation reservation1 = Reservation.builder()
//                .reservationName("이시현")
//                .reservationPhoneNumber("010-2314-2512")
//                .reservationState(ReservationState.COMPLETED)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now())
//                .reservationPrice(1000)
//                .reservationCapacity(10)
//                .studyRoom(studyRoom)
//                .member(member)
//                .build();
//
//        Reservation save = reservationRepository.save(reservation1);
//
//
//        Review review = Review.builder()
//                .reviewContent("치킨이 안보이네요..")
//                .reviewRating(1)
//                .reservation(save)
//                .workplaceName(workplace.getWorkplaceName().getValue())
//                .build();
//        reviewRepository.save(review);
//
//        mockMvc.perform(delete("/api/v1/review/{reviewId}?workplaceName=사업장 넘버원",review.getReviewId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//    @Test
//    @DisplayName("첫 페이지 요청 커서 없는 case")
//    void test5() throws Exception{
//
//
//        IntStream.rangeClosed(1, 20).forEach(i -> {
//
//
//            Reservation reservation = Reservation.builder()
//                    .reservationName("이시현")
//                    .reservationPhoneNumber("010-2314-2512")
//                    .reservationState(ReservationState.COMPLETED)
//                    .startTime(LocalDateTime.now())
//                    .endTime(LocalDateTime.now())
//                    .reservationCapacity(10)
//                    .reservationPrice(1000)
//                    .studyRoom(studyRoom)
//                    .member(member)
//                    .build();
//
//            Reservation reservation1 = reservationRepository.save(reservation);
//
//            Review review1 = Review.builder()
//                    .reviewContent("치킨이 안보이네요.." + i)
//                    .reviewRating(3 + i)
//                    .reservation(reservation1)
//                    .workplaceName(workplace.getWorkplaceName().getValue())
//                    .build();
//            reviewRepository.save(review1);
//
//            reservation1.addReview(review1);
//        });
//
//
//        mockMvc.perform(get("/api/v1/review/workplace/{workplaceId}",workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(jsonPath("$.data[9].reviewContent").value("치킨이 안보이네요..11"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("첫번쨰 페이지 요청시 나온 lastId를 사용하여  커사사용해서 페이징 처리")
//    void test6() throws Exception{
//
//
//        IntStream.rangeClosed(1, 20).forEach(i -> {
//
//
//            Reservation reservation = Reservation.builder()
//                    .reservationName("이시현")
//                    .reservationPhoneNumber("010-2314-2512")
//                    .reservationState(ReservationState.COMPLETED)
//                    .startTime(LocalDateTime.now())
//                    .endTime(LocalDateTime.now())
//                    .studyRoom(studyRoom)
//                    .reservationCapacity(10)
//                    .reservationPrice(1000)
//                    .member(member)
//                    .build();
//
//            Reservation reservation1 = reservationRepository.save(reservation);
//
//            Review review1 = Review.builder()
//                    .reviewContent("치킨이 안보이네요.." + i)
//                    .reviewRating(3 + i)
//                    .reservation(reservation1)
//                    .workplaceName(workplace.getWorkplaceName().getValue())
//                    .build();
//            reviewRepository.save(review1);
//
//            reservation1.addReview(review1);
//        });
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/review/workplace/{workplaceId}", workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//
//                )
//                .andReturn();
//
//        String json = mvcResult.getResponse().getContentAsString();
//        Long nestCursor = JsonPath.parse(json).read("$.nextCursor", Long.class);
//
//        mockMvc.perform(get("/api/v1/review/workplace/{workplaceId}?lastId="+nestCursor, workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(jsonPath("$.nextCursor").isNotEmpty())
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("유효하지 않은 LastId 사용시 null 반환")
//    void test7() throws Exception{
//
//
//
//        IntStream.rangeClosed(1, 20).forEach(i -> {
//
//
//            Reservation reservation = Reservation.builder()
//                    .reservationName("이시현")
//                    .reservationPhoneNumber("010-2314-2512")
//                    .reservationState(ReservationState.COMPLETED)
//                    .startTime(LocalDateTime.now())
//                    .endTime(LocalDateTime.now())
//                    .studyRoom(studyRoom)
//                    .reservationCapacity(10)
//                    .reservationPrice(1000)
//                    .member(member)
//                    .build();
//
//            Reservation reservation1 = reservationRepository.save(reservation);
//
//            Review review1 = Review.builder()
//                    .reviewContent("치킨이 안보이네요.." + i)
//                    .reviewRating(3 + i)
//                    .reservation(reservation1)
//                    .workplaceName(workplace.getWorkplaceName().getValue())
//                    .build();
//            reviewRepository.save(review1);
//
//            reservation1.addReview(review1);
//        });
//
//        mockMvc.perform(get("/api/v1/review/workplace/{workplaceId}?lastId=232",workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andDo(print());
//
//    }
//}