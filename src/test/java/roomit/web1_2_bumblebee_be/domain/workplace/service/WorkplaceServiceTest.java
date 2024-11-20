package roomit.web1_2_bumblebee_be.domain.workplace.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceInvalidRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotFound;
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
    private BusinessRepository businessRepository;

    @Autowired
    private WorkplaceService workplaceService;

    private Business savedBusiness;

    @BeforeEach
    void setUp() {
        // 기존 데이터를 모두 삭제하여 중복 방지
        businessRepository.deleteAll();

        // 고유한 business_email로 설정
        Business business = Business.builder()
                .businessName("Test Business")
                .businessPwd("securePassword123")
                .businessEmail("testbusiness" + System.currentTimeMillis() + "@example.com") // 고유 이메일
                .businessNum("123-45-67890")
                .build();

        savedBusiness = businessRepository.save(business);
    }


    @Test
    @DisplayName("사업장 등록 및 조회")
    @Order(1)
    void createAndReadWorkplace() {
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
                .business(savedBusiness) // 저장된 Business 객체를 참조
                .build();

        Workplace savedWorkplace = workplaceRepository.save(workplace); // Workplace 저장 후 반환된 객체 사용

        // When
        WorkplaceResponse findWorkplace = workplaceService.readWorkplace(savedWorkplace.getWorkplaceId()); // 저장된 ID로 조회

        // Then
        assertNotNull(findWorkplace);
        assertEquals("사업장", findWorkplace.getWorkplaceName());
        assertEquals("010-1234-1234", findWorkplace.getWorkplacePhoneNumber());
        assertEquals("대한민국", findWorkplace.getWorkplaceAddress());
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

        assertEquals("잘못된 입력입니다.", exception.getMessage());
    }


    @Test
    @DisplayName("사업장 조회 실패")
    @Order(3)
    void readWorkplaceFailed() {

        // When & Then
        assertThrows(WorkplaceNotFound.class, () -> {
            workplaceService.readWorkplace(10000L);
        });
    }

    @Test
    @DisplayName("사업장 수정")
    @Order(4)
    void updateWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("기존 사업장")
                .workplacePhoneNumber("010-1234-1234")
                .workplaceDescription("기존 설명")
                .workplaceAddress("기존 주소")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .business(savedBusiness) // 기존 Business 설정
                .build();

        Workplace savedWorkplace = workplaceRepository.save(workplace);

        Workplace updatedworkplace = Workplace.builder()
                .workplaceId(savedWorkplace.getWorkplaceId())
                .workplaceName("사업장 수정")
                .workplacePhoneNumber("010-1234-1230")
                .workplaceDescription("사업장 설명 수정")
                .workplaceAddress("대한민국")
                .profileImage(null)
                .imageType(null)
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .business(savedBusiness)
                .build();

        WorkplaceRequest workplaceDto = new WorkplaceRequest(updatedworkplace);

        // When
        workplaceService.updateWorkplace(savedWorkplace.getWorkplaceId(), workplaceDto);

        // Then
        WorkplaceResponse findWorkplace = workplaceService.readWorkplace(workplace.getWorkplaceId());
        assertNotNull(findWorkplace);
        assertEquals(findWorkplace.getWorkplaceName(), "사업장 수정");
        assertEquals(findWorkplace.getWorkplaceDescription(), "사업장 설명 수정");
    }

    @Test
    @DisplayName("사업장 수정 - 필수 필드 누락 실패")
    @Order(5)
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

        assertEquals("잘못된 입력입니다.", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("사업장 삭제")
    @Order(6)
    void deleteWorkplace() {
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

        Long workplaceId = workplaceRepository.save(workplace).getWorkplaceId();

        // Delete
        workplaceService.deleteWorkplace(workplaceId);

        // When & Then: 삭제된 Workplace를 조회하면 예외 발생
        assertThrows(WorkplaceNotFound.class, () -> {
            workplaceService.readWorkplace(workplaceId);
        });}

    @Test
    @DisplayName("사업장 목록 조회")
    @Order(7)
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
                    .business(savedBusiness)
                    .build();

            workplaceRepository.save(workplace);
        }

        // When
        List<WorkplaceResponse> workplaces = workplaceService.readAllWorkplaces();

        // Then
        assertThat(workplaces).isNotNull();
        assertThat(workplaces.size()).isEqualTo(100);
    }

    @Test
    @Order(8)
    @DisplayName("사업자 ID로 사업장 조회")
    void testFindWorkplacesByBusinessId() {
        // Given
        for (int i = 1; i <= 3; i++) {
            Workplace workplace = Workplace.builder()
                    .workplaceName("사업장 " + i)
                    .workplacePhoneNumber("010-1234-" + String.format("%04d", i))
                    .workplaceDescription("테스트 사업장 " + i)
                    .workplaceAddress("테스트 주소 " + i)
                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                    .business(savedBusiness) // Set the businessId
                    .build();

            workplaceRepository.save(workplace);
        }

        // When
        List<WorkplaceResponse> workplaces = workplaceService.findWorkplacesByBusinessId(savedBusiness.getBusinessId());

        // Then
        assertThat(workplaces).hasSize(3);
        assertThat(workplaces.get(0).getBusinessId()).isEqualTo(savedBusiness.getBusinessId());
        assertThat(workplaces.get(0).getWorkplaceName()).isEqualTo("사업장 1");
    }

//    @Test
//    void uploadImage() {
//    }
}