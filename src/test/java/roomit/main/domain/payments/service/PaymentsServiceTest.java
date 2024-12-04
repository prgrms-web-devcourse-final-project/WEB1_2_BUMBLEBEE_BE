//package roomit.main.domain.payments.service;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.client.RestTemplate;
//import roomit.main.domain.member.dto.CustomMemberDetails;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.payments.Service.PaymentsService;
//import roomit.main.domain.payments.config.PaymentsConfig;
//import roomit.main.domain.payments.dto.request.PaymentsRequest;
//import roomit.main.domain.payments.dto.response.PaymentValidationResponse;
//import roomit.main.domain.payments.dto.response.PaymentsFailResponse;
//import roomit.main.domain.payments.entity.Payments;
//import roomit.main.domain.payments.entity.TossPaymentMethod;
//import roomit.main.domain.payments.repository.PaymentsRepository;
//import roomit.main.domain.reservation.entity.Reservation;
//import roomit.main.domain.reservation.entity.ReservationState;
//import roomit.main.domain.reservation.repository.ReservationRepository;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.service.ImageService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test")
//public class PaymentsServiceTest {
//
//    @Autowired
//    private PaymentsService paymentsService;
//
//    @Autowired
//    private PaymentsRepository paymentsRepository;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private StudyRoomRepository studyRoomRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
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
//    private Reservation reservation;
//
//    private Workplace workplace;
//
//    private StudyRoom studyRoom;
//
//    private CustomMemberDetails customMemberDetails;
//
//    @BeforeAll
//    void setUp() {
//        date = LocalDate.of(2024, 11, 22);
//
//
//        workplace = Workplace.builder()
//                .workplaceName("Workplace")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("Workplace"))
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
//                .memberEmail("wfqdfqwf@naver.com")
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
//                .imageUrl(imageService.createImageUrl("Workplace/Test Room"))
//                .workplace(workplace)
//                .build();
//
//        studyRoomRepository.save(studyRoom);
//
//
//        reservation = Reservation.builder()
//                .reservationName("이시현")
//                .reservationPhoneNumber("010-2314-2512")
//                .reservationState(ReservationState.ON_HOLD)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now())
//                .studyRoom(studyRoom)
//                .member(member)
//                .reservationCapacity(123)
//                .reservationPrice(1000)
//                .build();
//
//        reservationRepository.save(reservation);
//
//        customMemberDetails = new CustomMemberDetails(member);
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    }
//
//    @Test
//    @DisplayName("결제 검증")
//    @Order(1)
//    void test1(){
//        //GIVEN
//        Long reservationId = reservation.getReservationId();
//        Long memberId = member.getMemberId();
//
//        PaymentsRequest paymentsRequest = PaymentsRequest.builder()
//                .orderId("order123")
//                .orderName("테스트주문")
//                .tossPaymentMethod(TossPaymentMethod.CARD)
//                .memberPhoneNum("010-1111-2222")
//                .memberName("이중호")
//                .totalAmount(1000L)
//                .build();
//
//        //WHEN
//        PaymentValidationResponse response = paymentsService.requestPayment(reservationId, memberId, paymentsRequest);
//
//        //THEN
//        assertNotNull(response);
//        assertEquals(paymentsConfig.getSuccessUrl(),response.successUrl());
//        assertEquals(paymentsConfig.getFailUrl(),response.failUrl());
//
//        Reservation updatedReservation = reservationRepository.findById(reservationId).orElseThrow();
//        assertEquals(ReservationState.COMPLETED, updatedReservation.getReservationState());
//
//        Payments savedPayment = paymentsRepository.findAll().stream().findFirst().orElseThrow();
//        assertEquals(paymentsRequest.totalAmount(), savedPayment.getTotalAmount());
//    }
//
//    @Test
//    @DisplayName("결제 실패")
//    @Order(2)
//    void test2() {
//
//        String code = "PAYMENT_FAILED";
//        String message = "결제 시간이 만료되었습니다.";
//        String orderId = "order123";
//
//        // WHEN
//        PaymentsFailResponse response = paymentsService.tossPaymentFail(code, message, orderId);
//
//        // THEN
//        Payments updatedPayment = paymentsRepository.findByOrderId(orderId).orElseThrow();
//        assertNotNull(response);
//        assertEquals(message, updatedPayment.getFailReason());
//        assertEquals(code, response.errorCode());
//        assertEquals(message, response.errorMessage());
//        assertEquals(orderId, response.orderId());
//    }
//}
