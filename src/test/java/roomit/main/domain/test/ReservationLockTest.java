//package roomit.main.domain.test;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.business.service.BusinessService;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
//import roomit.main.domain.reservation.entity.Reservation;
//import roomit.main.domain.reservation.repository.ReservationRepository;
//import roomit.main.domain.reservation.service.ReservationService;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.error.ErrorCode;
//import roomit.main.global.service.ImageService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.NoSuchElementException;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//public class ReservationLockTest {
//
//    @Autowired
//    private BusinessService businessService;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private StudyRoomRepository studyRoomRepository;
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Test
//    @DisplayName("분산락 x")
//    void test() throws InterruptedException {
//        LocalDate date = LocalDate.of(2024, 11, 22);
//
//        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                            .businessName("테스트사업자테스트" )
//                            .businessEmail("business8543@gmail.com")
//                            .businessPwd("Business1!")
//                            .businessNum("122-64-05126")
//                            .build();
//
//                    businessService.signUpBusiness(businessRegisterRequest); // 데이터 생성
//            Business business = businessRepository.findByBusinessEmail("business8543@gmail.com").orElseThrow(NoSuchElementException::new);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("Workplace")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("Workplace"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//        workplaceRepository.save(workplace);
//
//        StudyRoom studyRoom = StudyRoom.builder()
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
//        IntStream.rangeClosed(0, 9).forEach(i -> {
//            Member member =  Member.builder()
//                    .birthDay(date)
//                    .memberSex(Sex.FEMALE)
//                    .memberPwd("Business1!")
//                    .memberEmail("wfqdfqwfa"+i+"@naver.com")
//                    .memberPhoneNumber("010-1323-2154")
//                    .memberNickName("치킨유저")
//                    .passwordEncoder(passwordEncoder)
//                    .build();
//
//            memberRepository.save(member);
//                }
//        );
//        String code = "reservationTest";
//        Long studyRoomId = studyRoom.getStudyRoomId();
//
//        int numberOfThreads = 10;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//
//        CreateReservationRequest request = CreateReservationRequest.builder()
//                .reservationName("예약테스트")
//                .reservationPhoneNumber("010-1111-2222")
//                .capacity(4)
//                .price(50000)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(2))
//                .build();
//        System.out.println(studyRoomId);
//        System.out.println(request);
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            Long memberId = memberRepository.findByMemberEmail("wfqdfqwfa" + i + "@naver.com").orElseThrow().getMemberId();
//            System.out.println(memberId);
//            executorService.submit(() -> {
//                try {
//                    reservationService.createTestLockFalse(memberId,studyRoomId,request);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        Long totalCount = reservationRepository.count();
//
//        System.out.println("등록된 예약 = " + totalCount);
//        assertThat(totalCount).isOne();
//
//    }
//
//    @Test
//    @DisplayName("분산락 o")
//    void test2() throws InterruptedException {
//        LocalDate date = LocalDate.of(2024, 11, 22);
//
//        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                .businessName("테스트테스트사업자" )
//                .businessEmail("business1732@gmail.com")
//                .businessPwd("Business1!")
//                .businessNum("643-26-95312")
//                .build();
//
//        businessService.signUpBusiness(businessRegisterRequest); // 데이터 생성
//        Business business = businessRepository.findByBusinessEmail("business1732@gmail.com").orElseThrow(NoSuchElementException::new);
//
//        Workplace workplace = Workplace.builder()
//                .workplaceName("Workplace")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("Workplace"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//        workplaceRepository.save(workplace);
//
//        StudyRoom studyRoom = StudyRoom.builder()
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
//        IntStream.rangeClosed(0, 9).forEach(i -> {
//                    Member member =  Member.builder()
//                            .birthDay(date)
//                            .memberSex(Sex.FEMALE)
//                            .memberPwd("Business1!")
//                            .memberEmail("wfqdfqwf"+i+"@naver.com")
//                            .memberPhoneNumber("010-1323-2154")
//                            .memberNickName("치킨유저")
//                            .passwordEncoder(passwordEncoder)
//                            .build();
//
//                    memberRepository.save(member);
//                }
//        );
//        String code = "reservationTest";
//        Long studyRoomId = studyRoom.getStudyRoomId();
//
//        int numberOfThreads = 10;
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//
//        CreateReservationRequest request = CreateReservationRequest.builder()
//                .reservationName("예약테스트")
//                .reservationPhoneNumber("010-1111-2222")
//                .capacity(4)
//                .price(50000)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(2))
//                .build();
//        System.out.println(studyRoomId);
//        System.out.println(request);
//
//        String lockName = "예약 등록 키";
//
//        for (int i = 0; i < numberOfThreads; i++) {
//            Long memberId = memberRepository.findByMemberEmail("wfqdfqwf" + i + "@naver.com").orElseThrow().getMemberId();
//            System.out.println(memberId);
//            executorService.submit(() -> {
//                try {
//                    reservationService.createTestLockSuccess(lockName,memberId,studyRoomId,request);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        Long totalCount = reservationRepository.count();
//
//        System.out.println("등록된 예약 = " + totalCount);
//        assertThat(totalCount).isOne();
//    }
//
//}
