//package roomit.web1_2_bumblebee_be.domain.studyroom;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.CreateStudyRoomRequest;
//import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.FindPossibleStudyRoomRequest;
//import roomit.web1_2_bumblebee_be.domain.studyroom.dto.request.UpdateStudyRoomRequest;
//import roomit.web1_2_bumblebee_be.domain.studyroom.dto.response.FindPossibleStudyRoomResponse;
//import roomit.web1_2_bumblebee_be.domain.studyroom.entity.StudyRoom;
//import roomit.web1_2_bumblebee_be.domain.studyroom.repository.StudyRoomRepository;
//import roomit.web1_2_bumblebee_be.domain.studyroom.service.StudyRoomService;
//import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
//import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//public class StudyRoomServiceTest {
//
//    @Mock
//    private StudyRoomRepository studyRoomRepository;
//
//    @Mock
//    private WorkplaceRepository workplaceRepository;
//
//    @InjectMocks
//    private StudyRoomService studyRoomService;
//
//    private Workplace mockWorkplace;
//    private StudyRoom mockStudyRoom;
//
//    @BeforeEach
//    void setUp() {
//        mockWorkplace = Workplace.builder()
//                .workplaceName("Test Workplace")
//                .workplaceAddress("123 Test Address")
//                .build();
//
//        mockStudyRoom = StudyRoom.builder()
//                .title("Test Room")
//                .description("A test room")
//                .capacity(10)
//                .price(100)
//                .workPlaceId(mockWorkplace)
//                .build();
//    }
//
//    @Test
//    @DisplayName("생성 메서드 테스트")
//    void createStudyRoom_shouldCreateStudyRoom() {
//        // Given
//        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
//                .title("New Room")
//                .description("A new room")
//                .capacity(20)
//                .price(200)
//                .workplaceId(mockWorkplace.getWorkplaceId())
//                .build();
//
//        when(workplaceRepository.findById(request.getWorkplaceId())).thenReturn(Optional.of(mockWorkplace));
//
//        // When
//        studyRoomService.createStudyRoom(request);
//
//        // Then
//        verify(studyRoomRepository).save(any(StudyRoom.class));
//    }
//
//    @Test
//    void getAllStudyRooms_shouldReturnAllStudyRooms() {
//        // Given
//        when(studyRoomRepository.findAll()).thenReturn(Arrays.asList(mockStudyRoom));
//
//        // When
//        List<StudyRoom> studyRooms = studyRoomService.getAllStudyRooms();
//
//        // Then
//        assertEquals(1, studyRooms.size());
//        assertEquals(mockStudyRoom, studyRooms.get(0));
//    }
//
//    @Test
//    void getStudyRoom_shouldReturnStudyRoom() {
//        // Given
//        Long studyRoomId = 1L;
//        when(studyRoomRepository.findById(studyRoomId)).thenReturn(Optional.of(mockStudyRoom));
//
//        // When
//        StudyRoom studyRoom = studyRoomService.getStudyRoom(studyRoomId);
//
//        // Then
//        assertEquals(mockStudyRoom, studyRoom);
//    }
//
//    @Test
//    void updateStudyRoom_shouldUpdateStudyRoom() {
//        // Given
//        UpdateStudyRoomRequest request = UpdateStudyRoomRequest.builder()
//                .studyRoomId(mockStudyRoom.getStudyroomId())
//                .title("Updated Room")
//                .description("An updated room")
//                .capacity(30)
//                .price(300)
//                .build();
//
//        when(studyRoomRepository.findById(request.getStudyRoomId())).thenReturn(Optional.of(mockStudyRoom));
//
//        // When
//        studyRoomService.updateStudyRoom(request);
//
//        // Then
//        verify(studyRoomRepository).save(any(StudyRoom.class));
//    }
//
//    @Test
//    void deleteStudyRoom_shouldDeleteStudyRoom() {
//        // Given
//        Long studyRoomId = 1L;
//        when(studyRoomRepository.findById(studyRoomId)).thenReturn(Optional.of(mockStudyRoom));
//
//        // When
//        studyRoomService.deleteStudyRoom(studyRoomId);
//
//        // Then
//        verify(studyRoomRepository).delete(mockStudyRoom);
//    }
//
//    @Test
//    void findAvailableStudyRooms_shouldReturnAvailableStudyRooms() {
//        // Given
//        FindPossibleStudyRoomRequest request = FindPossibleStudyRoomRequest.builder()
//                .workplaceAddress("123 Test Address")
//                .startTime("2023-01-01T10:00:00")
//                .endTime("2023-01-01T12:00:00")
//                .capacity(5)
//                .build();
//
//        when(studyRoomRepository.findAvailableStudyRooms(request.getWorkplaceAddress(), request.getStartTime(), request.getEndTime(), request.getCapacity()))
//                .thenReturn(Arrays.asList(mockStudyRoom));
//
//        // When
//        List<FindPossibleStudyRoomResponse> availableStudyRooms = studyRoomService.findAvailableStudyRooms(request);
//
//        // Then
//        assertEquals(1, availableStudyRooms.size());
//        assertEquals(mockStudyRoom.getTitle(), availableStudyRooms.get(0).getStudyRoomTitle());
//    }
//}