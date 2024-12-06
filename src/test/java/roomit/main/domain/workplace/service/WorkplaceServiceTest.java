//package roomit.main.domain.workplace.service;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.business.service.BusinessService;
//import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
//import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
//import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
//import roomit.main.domain.workplace.dto.response.WorkplaceAllResponse;
//import roomit.main.domain.workplace.dto.response.WorkplaceBusinessResponse;
//import roomit.main.domain.workplace.dto.response.WorkplaceDetailResponse;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.entity.value.Coordinate;
//import roomit.main.domain.workplace.entity.value.WorkplaceName;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.error.ErrorCode;
//import roomit.main.global.exception.CommonException;
//import roomit.main.global.service.ImageService;
//
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class WorkplaceServiceTest {
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @Autowired
//    private BusinessService businessService;
//
//    @Autowired
//    private WorkplaceService workplaceService;
//
//    @Autowired
//    private ImageService imageService;
//
//    private Business savedBusiness;
//
//    @BeforeEach
//    void setUp() {
//        workplaceRepository.deleteAll();
//        businessRepository.deleteAll();
//
//        String email = "business12@gmail.com";
//
//        // 고유한 business_email로 설정
//        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                .businessName("테스트사업자")
//                .businessEmail(email)
//                .businessPwd("Business1!")
//                .businessNum("123-12-12345")
//                .build();
//
//        businessService.signUpBusiness(businessRegisterRequest);
//
//        savedBusiness = businessRepository.findByBusinessEmail(email)
//                .orElseThrow(RuntimeException::new);
//    }
//
//
//    @Test
//    @DisplayName("사업장 등록 및 조회")
//    @Order(1)
//    void createAndReadWorkplace() {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장 넘버원")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(savedBusiness)
//                .build();
//
//        Workplace savedWorkplace = workplaceRepository.save(workplace); // Workplace 저장 후 반환된 객체 사용
//
//        // When
//        WorkplaceDetailResponse findWorkplace = workplaceService.readWorkplace(savedWorkplace.getWorkplaceId()); // 저장된 ID로 조회
//
//        // Then
//        assertNotNull(findWorkplace);
//        assertEquals("사업장 넘버원", findWorkplace.workplaceName());
//        assertEquals("0507-1234-5678", findWorkplace.workplacePhoneNumber());
//        assertEquals("서울 중구 장충단로 247 굿모닝시티 8층", findWorkplace.workplaceAddress());
//    }
//
//
//    @Test
//    @DisplayName("사업장 등록")
//    @Order(2)
//    void createWorkplace() {
//        // Given
//        WorkplaceRequest workplace = WorkplaceRequest.builder()
//                .workplaceName("사업장1")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .studyRoomList(Arrays.asList(
//                        new CreateStudyRoomRequest(
//                                "Room A",
//                                "작은 룸",
//                                7000,
//                                4
//                        ),
//                        new CreateStudyRoomRequest(
//                                "Room B",
//                                "큰 룸",
//                                8000,
//                                6
//                        )
//                ))
//                .build();
//
//        // When
//        workplaceService.createWorkplace(workplace, savedBusiness.getBusinessId());
//        Workplace findWorkplace = workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName("사업장1"));
//
//        // Then
//        assertEquals("사업장1", findWorkplace.getWorkplaceName().getValue());
//        assertEquals("0507-1234-5678", findWorkplace.getWorkplacePhoneNumber().getValue());
//        assertEquals("서울 중구 장충단로 247 굿모닝시티 8층", findWorkplace.getWorkplaceAddress().getValue());
//    }
//
//    @Test
//    @DisplayName("사업장 등록 - 필수 필드 오류 실패")
//    @Order(3)
//    void createWorkplaceFailed() {
//        // Given
//        WorkplaceRequest workplace = WorkplaceRequest.builder()
//                .workplaceName("사업장@") // '@' 특수문자는 허용되지 않음
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .build();
//
//
//        // When & Then
//        CommonException exception = assertThrows(CommonException.class, () -> {
//            workplaceService.createWorkplace(workplace, savedBusiness.getBusinessId());
//        });
//
//        assertEquals("잘못된 입력입니다.", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("사업장 등록 - 스터디룸 등록 실패")
//    @Order(4)
//    void createWorkplaceStudyRoomFailed() {
//        // Given
//        WorkplaceRequest workplace = WorkplaceRequest.builder()
//                .workplaceName("사업장@") // '@' 특수문자는 허용되지 않음
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .studyRoomList(Arrays.asList(
//                        new CreateStudyRoomRequest(
//                                "Room A",
//                                "작은 룸",
//                                7000,
//                                4
//                        ),
//                        new CreateStudyRoomRequest(
//                                "Room B",
//                                "큰 룸",
//                                8000,
//                                6
//                        )
//                ))
//                .build();
//
//
//        // When & Then
//        CommonException exception = assertThrows(CommonException.class, () -> {
//            workplaceService.createWorkplace(workplace, savedBusiness.getBusinessId());
//        });
//
//        assertEquals("잘못된 입력입니다.", exception.getMessage());
//    }
//
//
//    @Test
//    @DisplayName("사업장 조회 실패")
//    @Order(5)
//    void readWorkplaceFailed() {
//
//        // When & Then
//        CommonException exception = assertThrows(CommonException.class, () -> {
//            workplaceService.readWorkplace(10000L);
//        });
//
//        assertEquals(ErrorCode.WORKPLACE_NOT_FOUND.getMessage(), exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("사업장 수정")
//    @Order(6)
//    void updateWorkplace() {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("기존 사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("기존 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("기존 사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(savedBusiness)
//                .build();
//
//        Workplace savedWorkplace = workplaceRepository.save(workplace);
//
//        WorkplaceRequest updatedworkplace = WorkplaceRequest.builder()
//                .workplaceName("사업장 수정")
//                .workplacePhoneNumber("0507-1234-5670")
//                .workplaceDescription("사업장 설명 수정")
//                .workplaceAddress("서울 중구 을지로 227 훈련원공원")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .build();
//
//        // When
//        System.out.println(savedWorkplace.getBusiness().getBusinessId());
//        System.out.println(savedBusiness.getBusinessId());
//        workplaceService.updateWorkplace(savedWorkplace.getWorkplaceId(), updatedworkplace, savedBusiness.getBusinessId());
//
//        // Then
//        WorkplaceDetailResponse findWorkplace = workplaceService.readWorkplace(workplace.getWorkplaceId());
//        assertNotNull(findWorkplace);
//        assertEquals(findWorkplace.workplaceName(), "사업장 수정");
//        assertEquals(findWorkplace.workplaceDescription(), "사업장 설명 수정");
//    }
//
//    @Test
//    @DisplayName("사업장 수정 - 필수 필드 누락 실패")
//    @Order(7)
//    void updateWorkplaceFailed() {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(savedBusiness)
//                .build();
//        workplaceRepository.save(workplace);
//
//        // Given: 필수 필드 중 일부 누락된 요청
//        WorkplaceRequest request = WorkplaceRequest.builder()
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("수정된 설명")
//                .workplaceAddress("서울 중구 을지로 227 훈련원공원")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .build();
//
//
//        // When & Then: WorkplaceInvalidRequest 예외 발생 확인
//        CommonException exception = assertThrows(CommonException.class, () -> {
//            workplaceService.updateWorkplace(workplace.getWorkplaceId(), request, savedBusiness.getBusinessId());
//        });
//
//        assertEquals(ErrorCode.WORKPLACE_NOT_MODIFIED.getMessage(), exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("사업장 삭제")
//    @Order(8)
//    void deleteWorkplace() {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(savedBusiness)
//                .build();
//
//        Long workplaceId = workplaceRepository.save(workplace).getWorkplaceId();
//
//        // Delete
//        workplaceService.deleteWorkplace(workplaceId, savedBusiness.getBusinessId());
//
//        // When & Then: 삭제된 Workplace를 조회하면 예외 발생
//        assertThrows(CommonException.class, () -> {
//            workplaceService.readWorkplace(workplaceId);
//        });
//    }
//
//    @Test
//    @DisplayName("사업장 목록 조회")
//    @Order(9)
//    void readAllWorkplaces() {
//        // Given
//        workplaceRepository.deleteAll();
//        List<String> addresses = Arrays.asList(
//                "서울특별시 중구 세종대로 110 서울시청",
//                "서울특별시 중구 을지로 30 시그니처타워",
//                "서울특별시 용산구 이태원로 29 서울드래곤시티",
//                "서울특별시 강남구 테헤란로 152 강남파이낸스센터",
//                "서울특별시 영등포구 국제금융로 10 서울국제금융센터",
//                "서울특별시 송파구 올림픽로 300 롯데월드타워",
//                "서울특별시 마포구 월드컵북로 396 상암동 DMC타워",
//                "서울특별시 강서구 마곡중앙로 161-8 LG사이언스파크",
//                "서울특별시 성동구 아차산로 113 성수동 헤이그라운드",
//                "서울특별시 서초구 서초대로 411 GT타워",
//                "서울 중구 장충단로 247 굿모닝시티 8층"
//        );
//
//        int cnt = 0;
//
//        for (int i = 1; i <= 11; i++) {
//            WorkplaceRequest workplace = WorkplaceRequest.builder()
//                    .workplaceName("사업장" + i)
//                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
//                    .workplaceDescription("사업장 설명")
//                    .workplaceAddress(addresses.get(i - 1))
//                    .workplaceStartTime(LocalTime.of(9, 0))
//                    .workplaceEndTime(LocalTime.of(18, 0))
//                    .studyRoomList(Arrays.asList(
//                            new CreateStudyRoomRequest(
//                                    "Room A",
//                                    "작은 룸",
//                                    7000,
//                                    4
//                            ),
//                            new CreateStudyRoomRequest(
//                                    "Room B",
//                                    "큰 룸",
//                                    8000,
//                                    6
//                            )
//                    ))
//                    .build();
//
//            // When
//            workplaceService.createWorkplace(workplace, savedBusiness.getBusinessId());
//            Workplace findWorkplace = workplaceRepository.getWorkplaceByWorkplaceName(new WorkplaceName("사업장" + i));
//
//            if (findWorkplace.getLocation().getY()<=38.56 &&
//                    findWorkplace.getLocation().getY()>=36.56&&
//                    findWorkplace.getLocation().getX()>=126.97 &&
//                    findWorkplace.getLocation().getX()<=127.97 ){
//                cnt++;
//            }
//        }
//
//        // When
//        WorkplaceGetRequest request = WorkplaceGetRequest.builder()
//                .topRight(Coordinate.builder()
//                        .latitude(38.56)
//                        .longitude(127.97).build())
//                .bottomLeft(Coordinate.builder()
//                        .latitude(36.56)
//                        .longitude(126.97).build())
//                .latitude(37.56)
//                .longitude(127.00)
//                .build();
//
//        List<WorkplaceAllResponse> workplaces = workplaceService.readAllWorkplaces(request);
//
//        // Then
//        assertThat(workplaces).isNotNull();
//        assertThat(workplaces.size()).isEqualTo(cnt);
//
//        // 거리값이 오름차순으로 정렬되었는지 확인
//        for (int i = 0; i < workplaces.size() - 1; i++) {
//            assertThat(workplaces.get(i).distance())
//                    .isLessThanOrEqualTo(workplaces.get(i + 1).distance());
//        }
//    }
//
//    @Test
//    @Order(10)
//    @DisplayName("사업자 ID로 사업장 조회")
//    void testFindWorkplacesByBusinessId() {
//        // Given
//        for (int i = 1; i <= 3; i++) {
//            Workplace workplace = Workplace.builder()
//                    .workplaceName("사업장 " + i)
//                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
//                    .workplaceDescription("테스트 사업장 " + i)
//                    .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                    .workplaceStartTime(LocalTime.of(9, 0))
//                    .workplaceEndTime(LocalTime.of(18, 0))
//                    .imageUrl(imageService.createImageUrl("사업장"))
//                    .business(savedBusiness) // Set the businessId
//                    .build();
//
//            workplaceRepository.save(workplace);
//        }
//
//        // When
//        WorkplaceBusinessResponse workplaces = workplaceService.findWorkplacesByBusinessId(savedBusiness.getBusinessId());
//
//        // Then
//        assertThat(workplaces.businessId()).isEqualTo(savedBusiness.getBusinessId());
//        assertThat(workplaces.workplaces().get(0).workplaceName()).isEqualTo("사업장 1");
//    }
//
//    @Test
//    @DisplayName("유효하지 않은 주소로 좌표 변환 실패")
//    @Order(11)
//    void testGeoCordingFailed() {
//        // Given: 유효하지 않은 주소 입력
//        WorkplaceRequest workplaceRequest = WorkplaceRequest.builder()
//                .workplaceName("유효하지 않은 사업장")
//                .workplaceAddress("Invalid Address") // 잘못된 주소
//                .workplacePhoneNumber("0507-1234-5678")
//                .build();
//
//        // When & Then: WORKPLACE_INVALID_ADDRESS 예외 발생
//        CommonException exception = assertThrows(CommonException.class, () -> {
//            workplaceService.getStringBigDecimalMap(workplaceRequest);
//        });
//
//        assertEquals(ErrorCode.WORKPLACE_INVALID_ADDRESS.getMessage(), exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("유효한 주소로 좌표 변환 성공")
//    @Order(12)
//    void testGeoCordingSuccess() {
//        // Given: 유효한 주소 입력
//        WorkplaceRequest workplaceRequest = WorkplaceRequest.builder()
//                .workplaceName("유효한 사업장")
//                .workplaceAddress("서울 중구 장충단로 247, 굿모닝시티몰 8층") // 유효한 주소
//                .workplacePhoneNumber("0507-1234-5678")
//                .build();
//
//        // When: 좌표 변환 시도
//        Map<String, Double> coordinates = workplaceService.getStringBigDecimalMap(workplaceRequest);
//
//        // Then: 좌표가 반환되는지 검증
//        assertNotNull(coordinates);
//        assertTrue(coordinates.containsKey("latitude"));
//        assertTrue(coordinates.containsKey("longitude"));
//        assertEquals("37.5668021171335", coordinates.get("latitude")); // 예시값
//        assertEquals("127.007358177138", coordinates.get("longitude")); // 예시값
//    }
//
//}
