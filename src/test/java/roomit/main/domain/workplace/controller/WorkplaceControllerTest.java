//package roomit.main.domain.workplace.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.NoSuchElementException;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.locationtech.jts.geom.CoordinateSequence;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.main.domain.business.entity.Business;
//import roomit.main.domain.business.repository.BusinessRepository;
//import roomit.main.domain.studyroom.dto.request.CreateStudyRoomRequest;
//import roomit.main.domain.workplace.dto.request.WorkplaceGetRequest;
//import roomit.main.domain.workplace.dto.request.WorkplaceRequest;
//import roomit.main.domain.workplace.entity.Workplace;
//import roomit.main.domain.workplace.entity.value.Coordinate;
//import roomit.main.domain.workplace.repository.WorkplaceRepository;
//import roomit.main.global.service.ImageService;
//import roomit.main.global.token.dto.request.LoginRequest;
//import roomit.main.global.util.PointUtil;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class WorkplaceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    @Autowired
//    private ImageService imageService;
//
//    private static String token;
//
//    private static Long workplaceId;
//
//    private Business business;
//
//    @BeforeAll
//    void setUp() throws Exception {
//        businessRepository.deleteAll();
//        workplaceRepository.deleteAll();
//
//        //회원 가입
//        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
//                .businessName("테스트사업자")
//                .businessEmail("business1@gmail.com")
//                .businessPwd("Business1!")
//                .businessNum("123-12-12347")
//                .build();
//
//        String json = objectMapper.writeValueAsString(businessRegisterRequest);
//
//        mockMvc.perform(post("/api/v1/business/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isCreated());
//
//        //로그인
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("business1@gmail.com")
//                .password("Business1!")
//                .build();
//
//        String loginJson = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/business")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        token = loginResult.getResponse().getHeader("Authorization");
//
//        business = businessRepository.findByBusinessEmail("business1@gmail.com").orElseThrow(NoSuchElementException::new);
//
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
//                "서울특별시 서초구 서초대로 411 GT타워"
//        );
//
//
//
//        for (int i = 1; i <= 10; i++) {
//            Workplace workplace = Workplace.builder()
//                    .workplaceName("사업장 " + i)
//                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
//                    .workplaceDescription("사업장 설명 " + i)
//                    .workplaceAddress(addresses.get(i - 1))
//                    .imageUrl(imageService.createImageUrl("사업장 " + i))
//                    .workplaceStartTime(LocalTime.of(9, 0))
//                    .workplaceEndTime(LocalTime.of(18, 0))
//                    .location(PointUtil.createPoint(127.0 + i * 0.01, 37.0 + i * 0.01))
//                    .business(business) // 이미 정의된 business 객체
//                    .build();
//
//
//            workplaceRepository.save(workplace);
//            workplaceId = workplace.getWorkplaceId();
//        }
//
//
//    }
//
//
//    @Order(1)
//    @Test
//    @DisplayName("사업장 등록")
//    void createWithMockMvc() throws Exception {
//        // Given
//        WorkplaceRequest workplace = WorkplaceRequest.builder()
//                .workplaceName("사업장1")
//                .workplacePhoneNumber("0507-1234-5698")
//                .workplaceDescription("사업장 설명1")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 7층")
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
//        String json = objectMapper.writeValueAsString(workplace);
//
//        // When
//        mockMvc.perform(post("/api/v1/workplace")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                // Then
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//
//    @Order(2)
//    @Test
//    @DisplayName("사업장 정보")
//    void getWorkplace() throws Exception {
//        // Given
//
//        // 토큰 출력 (디버깅용)
//        System.out.println("Generated Token: " + token);
//
//        // When
//        mockMvc.perform(get("/api/v1/workplace/info/{workplaceId}", workplaceId)
//                        .header("Authorization", "Bearer " + token) // 토큰 추가
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//
//                // Then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.workplaceName").value("사업장 10"))
//                .andDo(print());
//    }
//
//    @Order(3)
//    @Test
//    @DisplayName("사업장 수정")
//    void update() throws Exception {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//
//        workplaceRepository.save(workplace);
//
//        WorkplaceRequest updatedWorkplace = WorkplaceRequest.builder()
//                .workplaceName("사업장 수정")
//                .workplacePhoneNumber("0507-1234-5670")
//                .workplaceDescription("사업장 설명 수정")
//                .workplaceAddress("서울 중구 장충단로13길 20")
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .build();
//
//        String json = objectMapper.writeValueAsString(updatedWorkplace);
//
//        System.out.println("Generated JSON: " + json); // JSON 출력 확인
//
//        // When
//        mockMvc.perform(put("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
//                        .header("Authorization", "Bearer " + token) // 토큰 추가
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                // Then
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Order(4)
//    @Test
//    @DisplayName("사업장 삭제")
//    void deleteWorkplace() throws Exception {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
//                .imageUrl(imageService.createImageUrl("사업장"))
//                .workplaceStartTime(LocalTime.of(9, 0))
//                .workplaceEndTime(LocalTime.of(18, 0))
//                .business(business)
//                .build();
//
//        workplaceRepository.save(workplace);
//
//
//        // When
//        mockMvc.perform(delete("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)) // 토큰 추가
//                // Then
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Order(5)
//    @Test
//    @DisplayName("사업장 목록 조회")
//    void getWorkplaces() throws Exception {
//        // Given
////        workplaceRepository.deleteAll();
//        Double minLatitude = 36.56;
//        Double maxLatitude = 38.56;
//        Double minLongitude = 126.97;
//        Double maxLongitude = 127.97;
//
//        WorkplaceGetRequest request = WorkplaceGetRequest.builder()
//                .topRight(Coordinate.builder()
//                        .latitude(maxLatitude)
//                        .longitude(maxLongitude)
//                        .build())
//                .bottomLeft(Coordinate.builder()
//                        .latitude(minLatitude)
//                        .longitude(minLongitude)
//                        .build())
//                .latitude(37.56)
//                .longitude(127.00)
//                .build();
//
//        String requestBody = objectMapper.writeValueAsString(request);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/workplace/distance")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*].latitude").value(Matchers.everyItem(Matchers.allOf(
//                        Matchers.greaterThanOrEqualTo(minLatitude),
//                        Matchers.lessThanOrEqualTo(maxLatitude)
//                )))) // latitude 범위 확인
//                .andExpect(jsonPath("$[*].longitude").value(Matchers.everyItem(Matchers.allOf(
//                        Matchers.greaterThanOrEqualTo(minLongitude),
//                        Matchers.lessThanOrEqualTo(maxLongitude)
//                )))) // longitude 범위 확인
//                .andDo(result -> {
//                    // 응답 JSON 확인 (디버깅용)
//                    System.out.println("Response Body: " + result.getResponse().getContentAsString());
//                })
//                .andDo(print());
//}
//
//    @Order(6)
//    @Test
//    @DisplayName("사업자 ID로 목록 조회")
//    void getWorkplacesByBusinessId() throws Exception {
//        // Given
//        Long businessId = business.getBusinessId(); // 테스트할 사업자 ID
//        String expectedBusinessName = "테스트사업자";
//
//        // When
//        mockMvc.perform(get("/api/v1/workplace/business")
//                        .header("Authorization", "Bearer " + token) // 인증 토큰 추가
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//
//                // Then
//                .andExpect(status().isOk()) // HTTP 200 상태 코드 검증
//                .andExpect(jsonPath("$.businessId").value(businessId)) // businessId 검증
//                .andExpect(jsonPath("$.businessName").value(expectedBusinessName)) // businessName 검증
//                .andExpect(jsonPath("$.workplaces").isArray()) // workplaces가 배열인지 확인
//                .andExpect(jsonPath("$.workplaces[0].workplaceName").value("사업장 1")) // workplaces[0]의 workplaceName 확인
//                .andExpect(jsonPath("$.workplaces[1].workplaceName").value("사업장 2")) // workplaces[1]의 workplaceName 확인
//                .andDo(print());
//    }
//}