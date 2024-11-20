package roomit.web1_2_bumblebee_be.domain.workplace.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceInvalidRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotDelete;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotFound;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceTaskException;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkplaceServiceTest {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private WorkplaceService workplaceService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("사업장 등록")
    @Order(1)
    void createWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        WorkplaceRequest workplaceDto = new WorkplaceRequest(workplace);

        // When
        workplaceService.createWorkplace(workplaceDto);

        // Then
        WorkplaceResponse findWorkplace = workplaceService.readWorkplace(1L);
        assertNotNull(findWorkplace);
        assertEquals("사업장",findWorkplace.getWorkplaceName());
    }

    @Test
    @DisplayName("사업장 등록 - 필수 필드 누락 실패")
    @Order(2)
    void createWorkplaceFailed() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        WorkplaceRequest workplaceDto = new WorkplaceRequest(workplace);

        // When & Then: WorkplaceInvalidRequest 예외 발생 확인
        WorkplaceInvalidRequest exception = assertThrows(WorkplaceInvalidRequest.class, () -> {
            workplaceService.createWorkplace(workplaceDto);
        });

        assertEquals("사업장 이름을 입력해주세요.", exception.getMessage());
    }


    @Test
    @DisplayName("사업장 조회")
    @Order(3)
    void readWorkplace() {
        // Given

        // When
        WorkplaceResponse workplace = workplaceService.readWorkplace(1L);

        // Then
        assertThat(workplace).isNotNull();
        assertThat(workplace.getWorkplaceName()).isEqualTo("사업장");
        assertThat(workplace.getWorkplacePhoneNumber()).isEqualTo("010-1234-1234");
        assertThat(workplace.getWorkplaceAddress()).isEqualTo("대한민국");
    }

    @Test
    @DisplayName("사업장 조회 실패")
    @Order(4)
    void readWorkplaceFailed() {

        // When & Then
        assertThrows(WorkplaceNotFound.class, () -> {
            workplaceService.readWorkplace(10000L);
        });
    }

    @Test
    @DisplayName("사업장 수정")
    @Order(5)
    void updateWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceId(1L)
                .workplaceName("사업장 수정")
                .workplacePhoneNumber("010-1234-1230")
                .workplaceDescription("사업장 설명 수정")
                .workplaceAddress("대한민국")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        WorkplaceRequest workplaceDto = new WorkplaceRequest(workplace);

        // When
        workplaceService.updateWorkplace(1L, workplaceDto);

        // Then
        WorkplaceResponse findWorkplace = workplaceService.readWorkplace(1L);
        assertNotNull(findWorkplace);
        assertEquals(findWorkplace.getWorkplaceName(), "사업장 수정");
        assertEquals(findWorkplace.getWorkplaceDescription(), "사업장 설명 수정");
    }

    @Test
    @DisplayName("사업장 수정 - 필수 필드 누락 실패")
    @Order(6)
    void updateWorkplaceFailed() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();
        workplaceRepository.save(workplace);

        // Given: 필수 필드 중 일부 누락된 요청
        Workplace request = Workplace.builder()
                .workplacePhoneNumber("010-1234-5678")
                .workplaceDescription("수정된 설명")
                .workplaceAddress("수정된 주소")
                .workplaceStartTime(LocalDateTime.of(2023, 2, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 2, 1, 18, 0))
                .build();

        WorkplaceRequest workplaceDto = new WorkplaceRequest(request);


        // When & Then: WorkplaceInvalidRequest 예외 발생 확인
        WorkplaceInvalidRequest exception = assertThrows(WorkplaceInvalidRequest.class, () -> {
            workplaceService.updateWorkplace(workplace.getWorkplaceId(), workplaceDto);
        });

        assertEquals("사업장 이름을 입력해주세요.", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("사업장 삭제")
    @Order(7)
    void deleteWorkplace() {
        // Given: Workplace가 존재하는 상태에서 삭제
        Long workplaceId = 1L;
        workplaceService.deleteWorkplace(workplaceId);

        // When & Then: 삭제된 Workplace를 조회하면 예외 발생
        assertThrows(WorkplaceNotDelete.class, () -> {
            workplaceService.readWorkplace(workplaceId);
        });    }

    @Test
    @DisplayName("사업장 목록 조회")
    @Order(8)
    void readAllWorkplaces() {
        // Given
        workplaceRepository.deleteAll();
        for (int i = 1; i <= 100; i++) {
            Workplace workplace = Workplace.builder()
                    .workplaceName("사업장 " + i)
                    .workplacePhoneNumber("010-1234-" + String.format("%04d", i))
                    .workplaceDescription("테스트 사업장 " + i)
                    .workplaceAddress("테스트 주소 " + i)
                    .profileImage(null) // 이미지 생략 가능
                    .imageType("image/png")
                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                    .build();

            workplaceRepository.save(workplace);
        }

        // When
        List<WorkplaceResponse> workplaces = workplaceService.readAllWorkplaces();

        // Then
        assertThat(workplaces).isNotNull();
        assertThat(workplaces.size()).isEqualTo(100);
    }

//    @Test
//    void uploadImage() {
//    }
}