//package roomit.main.domain.reservation.service;
//
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.domain.reservation.dto.request.CreateReservationRequest;
//import roomit.main.domain.reservation.dto.request.UpdateReservationRequest;
//import roomit.main.domain.reservation.dto.response.MyWorkPlaceReservationResponse;
//import roomit.main.domain.reservation.dto.response.ReservationResponse;
//import roomit.main.domain.reservation.entity.Reservation;
//import roomit.main.domain.reservation.entity.ReservationState;
//import roomit.main.domain.reservation.repository.ReservationRepository;
//import roomit.main.domain.studyroom.entity.StudyRoom;
//import roomit.main.domain.studyroom.entity.value.StudyRoomName;
//import roomit.main.domain.studyroom.repository.StudyRoomRepository;
//import roomit.main.domain.workplace.entity.value.WorkplaceName;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//
//@SpringBootTest
//@Transactional
//public class ReservationServiceTest {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @MockBean
//    private ReservationRepository reservationRepository;
//
//    @MockBean
//    private WorkplaceRepository workplaceRepository;
//
//    @MockBean
//    private StudyRoomRepository studyRoomRepository;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    private Member member;
//    private StudyRoom studyRoom;
//    private Reservation reservation;
//    private CreateReservationRequest createRequest;
//    private UpdateReservationRequest updateRequest;
//
//    @BeforeEach
//    void setUp() {
//        LocalDate date = LocalDate.of(2024, 11, 22);
//
//
//        member =  Member.builder()
//                .birthDay(date)
//                .memberSex(Sex.FEMALE)
//                .memberPwd("Business1!")
//                .memberEmail("qwdfasdf@naver.com")
//                .memberPhoneNumber("010-2421-2315")
//                .memberNickName("치킨유저1")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();
//
//        studyRoom = new StudyRoom(1L,new StudyRoomName("StudyRoomA"), "Study Room A good", 10, 50000, "http://example.com/image.jpg", null);
//        reservation = new Reservation("User1","010-2222-2222" ,ReservationState.COMPLETED,10,50000,LocalDateTime.now(), LocalDateTime.now().plusHours(2),member,studyRoom);
//
//        createRequest = new CreateReservationRequest(
//                "John Doe",
//                "010-1234-5678",
//                10,
//                50000,
//                LocalDateTime.now().plusDays(1),
//                LocalDateTime.now().plusDays(1).plusHours(2)
//        );
//
//        updateRequest = new UpdateReservationRequest(
//                "Jane Doe",
//                "010-9876-5432",
//                LocalDateTime.now().plusDays(2),
//                LocalDateTime.now().plusDays(2).plusHours(2)
//        );
//    }
//
//    // 예약 생성 테스트
//    @Test
//    void createReservationTest() {
//        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
//        when(studyRoomRepository.findById(anyLong())).thenReturn(Optional.of(studyRoom));
//        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
//
//        reservationService.createReservation(1L, 1L, createRequest);
//
//        verify(reservationRepository, times(1)).save(any(Reservation.class));
//    }
//
//    // 예약 삭제 테스트
//    @Test
//    void deleteReservationTest() {
//        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
//
//        reservationService.deleteReservation(1L);
//
//        verify(reservationRepository, times(1)).delete(any(Reservation.class));
//    }
//
//    // 예약 수정 테스트
//    @Test
//    void updateReservationTest() {
//        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
//
//        reservationService.updateReservation(1L, updateRequest);
//
//        verify(reservationRepository, times(1)).save(any(Reservation.class));
//    }
//
//    // 특정 멤버의 최근 예약 조회 테스트
//    @Test
//    void findByMemberIdTest() {
//        when(reservationRepository.findRecentReservationByMemberId(anyLong())).thenReturn(List.of(reservation));
//        when(studyRoomRepository.findById(anyLong())).thenReturn(Optional.of(studyRoom));
//
//        ReservationResponse response = reservationService.findByMemberId(1L);
//
//        verify(reservationRepository, times(1)).findRecentReservationByMemberId(anyLong());
//        assert response != null;
//        assert response.workplaceName().equals(new WorkplaceName("Workplace A"));
//    }
//
//    // 특정 멤버의 모든 예약 조회 테스트
//    @Test
//    void findReservationsByMemberIdTest() {
//        when(reservationRepository.findReservationsByMemberId(anyLong())).thenReturn(List.of(reservation));
//        when(studyRoomRepository.findById(anyLong())).thenReturn(Optional.of(studyRoom));
//
//        List<ReservationResponse> responses = reservationService.findReservationsByMemberId(1L);
//
//        verify(reservationRepository, times(1)).findReservationsByMemberId(anyLong());
//        assert responses != null && !responses.isEmpty();
//        assert responses.get(0).workplaceName().equals(new WorkplaceName("Workplace A"));
//    }
//
//    // 특정 사업장의 예약자 조회 테스트
//    @Test
//    void findReservationByWorkplaceIdTest() {
//        when(reservationRepository.findMyWorkPlaceReservationsByWorkPlaceId(anyLong())).thenReturn(List.of(reservation));
//        when(studyRoomRepository.findById(anyLong())).thenReturn(Optional.of(studyRoom));
//
//        List<MyWorkPlaceReservationResponse> responses = reservationService.findReservationByWorkplaceId(1L);
//
//        verify(reservationRepository, times(1)).findMyWorkPlaceReservationsByWorkPlaceId(anyLong());
//        assert responses != null && !responses.isEmpty();
//        assert responses.get(0).workplaceName().equals(new WorkplaceName("Workplace A"));
//    }
//}
