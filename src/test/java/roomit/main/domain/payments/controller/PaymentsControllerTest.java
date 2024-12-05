//package roomit.main.domain.payments.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.payments.Service.PaymentsService;
//import roomit.main.domain.payments.config.PaymentsConfig;
//import roomit.main.domain.payments.dto.request.PaymentsRequest;
//import roomit.main.domain.payments.dto.response.PaymentValidationResponse;
//import roomit.main.domain.payments.entity.TossPaymentMethod;
//import roomit.main.domain.payments.repository.PaymentsRepository;
//import roomit.main.domain.reservation.entity.Reservation;
//import roomit.main.domain.reservation.entity.ReservationState;
//import roomit.main.domain.reservation.repository.ReservationRepository;
//import roomit.main.domain.review.repository.ReviewRepository;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.service.ImageService;
//import roomit.main.global.token.dto.request.LoginRequest;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test")
//public class PaymentsControllerTest {
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
//    private PaymentsRepository paymentsRepository;
//
//    @Autowired
//    private PaymentsService paymentsService;
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private PaymentsConfig paymentsConfig;
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
//        workplace = Workplace.builder()
//                .workplaceName("사업장 넘버원")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장 넘버원"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(null)
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
//                .reservationName("이중호")
//                .reservationPhoneNumber("010-1111-2222")
//                .reservationState(ReservationState.ON_HOLD)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now())
//                .studyRoom(studyRoom)
//                .reservationCapacity(10)
//                .reservationPrice(1000)
//                .member(member)
//                .build();
//
//        reservationRepository.save(reservation);
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
//    }
//
//    @Test
//    @DisplayName("결제 검증")
//    @Order(1)
//    void test1() throws Exception {
//        // GIVEN
//        PaymentsRequest paymentsRequest = PaymentsRequest.builder()
//                .orderId("order123")
//                .orderName("테스트주문")
//                .tossPaymentMethod(TossPaymentMethod.CARD)
//                .memberPhoneNum("010-1111-2222")
//                .memberName("이중호")
//                .totalAmount(1000L)
//                .build();
//
//        // WHEN & THEN
//        mockMvc.perform(post("/api/v1/payments/toss")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                        .param("reservationId", String.valueOf(reservation.getReservationId()))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(paymentsRequest)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("결제 실패")
//    @Order(2)
//    void test2() throws Exception {
//
//        //GIVEN
//        String code = "PAYMENT_FAILED";
//        String message = "결제 시간이 만료되었습니다.";
//        String orderId = "order123";
//
//        // WHEN
//        mockMvc.perform(get("/api/v1/payments/toss/fail")  // 실패 API 경로에 맞게 수정
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)  // 인증 토큰
//                        .param("orderId", orderId)  // 주문 ID 전달
//                        .param("code", code)  // 에러 코드
//                        .param("message", message)  // 에러 메시지
//                        .contentType(MediaType.APPLICATION_JSON))  // ContentType 설정
//                .andExpect(status().isOk());  // HTTP 상태 코드 검증
//    }
//}
