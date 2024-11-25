//package roomit.web1_2_bumblebee_be.domain.workplace.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import roomit.web1_2_bumblebee_be.domain.business.dto.request.BusinessRegisterRequest;
//import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
//import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
//import roomit.web1_2_bumblebee_be.domain.business.service.BusinessService;
//import roomit.web1_2_bumblebee_be.domain.member.service.MemberService;
//import roomit.web1_2_bumblebee_be.domain.workplace.dto.WorkplaceRequest;
//import roomit.web1_2_bumblebee_be.domain.workplace.entity.Workplace;
//import roomit.web1_2_bumblebee_be.domain.workplace.repository.WorkplaceRepository;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
//    private BusinessService businessService;
//
//    @Autowired
//    private WorkplaceRepository workplaceRepository;
//
//    private Business savedBusiness;
//
//    @Autowired
//    private MemberService memberService;
//
//    @BeforeEach
//    void setUp() {
//        workplaceRepository.deleteAll();
//        businessRepository.deleteAll();
//
//        String email = "business15@gmail.com";
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
//
//    }
//
//
//    @Order(1)
//    @Test
//    @DisplayName("사업장 등록")
//    void create() throws Exception {
//        // Given
//        WorkplaceRequest workplace = WorkplaceRequest.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국 서울시")
//                .imageUrl("http://image.url")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//        String json = objectMapper.writeValueAsString(workplace);
//
//        // When
//        mockMvc.perform(post("/api/v1/workplace/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//
//        // Then
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("workplace created"))
//                .andDo(print());
//    }
//
//    @Order(2)
//    @Test
//    @DisplayName("사업장 정보")
//    void getWorkplace() throws Exception {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("0507-1234-5678")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국 서울시")
//                .imageUrl("http://image.url")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .business(savedBusiness)
//                .build();
//
//        workplaceRepository.save(workplace);
//
//        // When
//        mockMvc.perform(get("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
//                .contentType(MediaType.APPLICATION_JSON)
//                )
//
//        // Then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.workplaceName").value("사업장"))
//                .andExpect(jsonPath("$.workplacePhoneNumber").value("0507-1234-5678"))
//                .andExpect(jsonPath("$.workplaceDescription").value("사업장 설명"))
//                .andExpect(jsonPath("$.workplaceAddress").value("대한민국 서울시"))
//                .andExpect(jsonPath("$.workplaceStartTime").value("2023-01-01T09:00:00"))
//                .andExpect(jsonPath("$.workplaceEndTime").value("2023-01-01T18:00:00"))
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
//                .workplaceAddress("대한민국 서울시")
//                .imageUrl("http://image.url")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//        workplaceRepository.save(workplace);
//
//        WorkplaceRequest updatedWorkplace = WorkplaceRequest.builder()
//                .workplaceName("사업장 수정")
//                .workplacePhoneNumber("0507-1234-5670")
//                .workplaceDescription("사업장 설명 수정")
//                .workplaceAddress("중국 상하이")
//                .imageUrl("http://image.url")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 8, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 19, 0))
//                .build();
//
//        String json = objectMapper.writeValueAsString(updatedWorkplace);
//
//        System.out.println("Generated JSON: " + json); // JSON 출력 확인
//
//        // When
//        mockMvc.perform(put("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//        // Then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("workplace updated"))
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
//                .workplaceAddress("대한민국 서울시")
//                .imageUrl("http://image.url")
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//        workplaceRepository.save(workplace);
//
//        // When
//        mockMvc.perform(delete("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//
//        // Then
//                .andExpect(status().isNoContent())
//                .andExpect(jsonPath("$.message").value("workplace deleted"))
//                .andDo(print());
//
//        assertEquals(0, workplaceRepository.count());
//    }
//
//    @Order(5)
//    @Test
//    @DisplayName("사업장 목록 조회")
//    void getWorkplaces() throws Exception {
//        // Given
//        for (int i = 1; i <= 100; i++) {
//            Workplace workplace = Workplace.builder()
//                    .workplaceName("사업장 " + i)
//                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
//                    .workplaceDescription("사업장 설명 " + i)
//                    .workplaceAddress("대한민국 서울시 " + i)
//                    .imageUrl("http://image.url")
//                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                    .business(savedBusiness)
//                    .build();
//
//            workplaceRepository.save(workplace);
//        }
//
//        // When
//        mockMvc.perform(get("/api/v1/workplace")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//        // Then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(100)) // 데이터 개수 검증
//                .andExpect(jsonPath("$[0].workplaceName").value("사업장 1"))
//                .andExpect(jsonPath("$[99].workplaceName").value("사업장 100")) // 마지막 데이터 확인
//                .andDo(print());
//    }
//
//
//}