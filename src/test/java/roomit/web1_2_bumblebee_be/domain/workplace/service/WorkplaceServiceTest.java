package roomit.web1_2_bumblebee_be.domain.workplace.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import roomit.web1_2_bumblebee_be.domain.business.dto.request.BusinessRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.service.BusinessService;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceResponse;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
import roomit.web1_2_bumblebee_be.domain.workplace.entity.value.WorkplaceName;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkplaceNotFound;
import roomit.web1_2_bumblebee_be.domain.workplace.exception.WorkspaceNotModified;
import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;
import roomit.web1_2_bumblebee_be.global.exception.CommonException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkplaceServiceTest {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private WorkplaceService workplaceService;

    private Business savedBusiness;

    @BeforeEach
    void setUp() {
        workplaceRepository.deleteAll();
        businessRepository.deleteAll();

        String email = "business12@gmail.com";

        // 고유한 business_email로 설정
        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
                .businessName("테스트사업자")
                .businessEmail(email)
                .businessPwd("Business1!")
                .businessNum("123-12-12345")
                .build();

        businessService.signUpBusiness(businessRegisterRequest);

        savedBusiness = businessRepository.findByBusinessEmail(email)
                .orElseThrow(RuntimeException::new);
    }


    @Test
    @DisplayName("사업장 등록 및 조회")
    @Order(1)
    void createAndReadWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장 넘버원")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .business(savedBusiness)
                .build();

        Workplace savedWorkplace = workplaceRepository.save(workplace); // Workplace 저장 후 반환된 객체 사용

        // When
        WorkplaceResponse findWorkplace = workplaceService.readWorkplace(savedWorkplace.getWorkplaceId()); // 저장된 ID로 조회

        // Then
        assertNotNull(findWorkplace);
        assertEquals("사업장 넘버원", findWorkplace.getWorkplaceName());
        assertEquals("0507-1234-5678", findWorkplace.getWorkplacePhoneNumber());
        assertEquals("대한민국 서울시", findWorkplace.getWorkplaceAddress());
    }



    @Test
    @DisplayName("사업장 등록")
    @Order(2)
    void createWorkplace() {
        // Given
        WorkplaceRequest workplace = WorkplaceRequest.builder()
                .workplaceName("사업장1")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        // When
        workplaceService.createWorkplace(workplace);
        Workplace findWorkplace = workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName("사업장1"));

        // Then
        assertEquals("사업장1", findWorkplace.getWorkplaceName().getValue());
        assertEquals("0507-1234-5678", findWorkplace.getWorkplacePhoneNumber().getValue());
        assertEquals("대한민국 서울시", findWorkplace.getWorkplaceAddress().getValue());
    }

    @Test
    @DisplayName("사업장 등록 - 필수 필드 오류 실패")
    @Order(3)
    void createWorkplaceFailed() {
        // Given
        WorkplaceRequest workplace = WorkplaceRequest.builder()
                .workplaceName("사업장@") // '@' 특수문자는 허용되지 않음
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();


        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workplaceService.createWorkplace(workplace);
        });

        assertEquals("사업장명은 특수문자를 제외한 1~20자리여야 하며, 띄워쓰기가 가능합니다.", exception.getMessage());
    }



    @Test
    @DisplayName("사업장 조회 실패")
    @Order(3)
    void readWorkplaceFailed() {

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> {
            workplaceService.readWorkplace(10000L);
        });

        assertEquals(ErrorCode.WORKPLACE_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("사업장 수정")
    @Order(4)
    void updateWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("기존 사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("기존 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .business(savedBusiness) // 기존 Business 설정
                .build();

        Workplace savedWorkplace = workplaceRepository.save(workplace);

        WorkplaceRequest updatedworkplace = WorkplaceRequest.builder()
                .workplaceName("사업장 수정")
                .workplacePhoneNumber("0507-1234-5670")
                .workplaceDescription("사업장 설명 수정")
                .workplaceAddress("대한민국 서울시 수정")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        // When
        workplaceService.updateWorkplace(savedWorkplace.getWorkplaceId(), updatedworkplace);

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
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();
        workplaceRepository.save(workplace);

        // Given: 필수 필드 중 일부 누락된 요청
        WorkplaceRequest request = WorkplaceRequest.builder()
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("수정된 설명")
                .workplaceAddress("대한민국 서울시 수정")
                .workplaceStartTime(LocalDateTime.of(2023, 2, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 2, 1, 18, 0))
                .build();


        // When & Then: WorkplaceInvalidRequest 예외 발생 확인
        CommonException exception = assertThrows(CommonException.class, () -> {
            workplaceService.updateWorkplace(workplace.getWorkplaceId(), request);
        });

        assertEquals(ErrorCode.WORKPLACE_NOT_MODIFIED.getMessage(), exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("사업장 삭제")
    @Order(6)
    void deleteWorkplace() {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("대한민국 서울시")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        Long workplaceId = workplaceRepository.save(workplace).getWorkplaceId();

        // Delete
        workplaceService.deleteWorkplace(workplaceId);

        // When & Then: 삭제된 Workplace를 조회하면 예외 발생
        assertThrows(CommonException.class, () -> {
            workplaceService.readWorkplace(workplaceId);
        });
    }

    @Test
    @DisplayName("사업장 목록 조회")
    @Order(7)
    void readAllWorkplaces() {
        // Given
        workplaceRepository.deleteAll();

        for (int i = 1; i <= 100; i++) {
            Workplace workplace = Workplace.builder()
                    .workplaceName("사업장 " + i)
                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
                    .workplaceDescription("테스트 사업장 " + i)
                    .workplaceAddress("테스트 주소 " + i)
                    .imageUrl("http://image.url")
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
                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
                    .workplaceDescription("테스트 사업장 " + i)
                    .workplaceAddress("테스트 주소 " + i)
                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                    .imageUrl("http://image.url")
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