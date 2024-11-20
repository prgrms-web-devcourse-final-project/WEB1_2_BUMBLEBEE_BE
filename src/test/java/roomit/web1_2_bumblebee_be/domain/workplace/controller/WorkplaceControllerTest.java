//package roomit.web1_2_bumblebee_be.domain.workplace.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
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
//@TestPropertySource(locations = "classpath:/application-test.properties")
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
//    private WorkplaceRepository workplaceRepository;
//
//    @BeforeEach
//    void setUp() {
//        workplaceRepository.deleteAll();
//    }
//
//
//    @Order(1)
//    @Test
//    @DisplayName("사업장 등록")
//    void create() throws Exception {
//        // Given
//        Workplace workplace = Workplace.builder()
//                .workplaceName("사업장")
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//        WorkplaceRequest workplaceDto = new WorkplaceRequest(workplace);
//
//        String json = objectMapper.writeValueAsString(workplaceDto);
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
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
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
//                .andExpect(jsonPath("$.workplacePhoneNumber").value("010-1234-1234"))
//                .andExpect(jsonPath("$.workplaceDescription").value("사업장 설명"))
//                .andExpect(jsonPath("$.workplaceAddress").value("대한민국"))
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
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
//                .build();
//
//        workplaceRepository.save(workplace);
//
//        Workplace updatedWorkplace = Workplace.builder()
//                .workplaceId(1L)
//                .workplaceName("사업장 수정")
//                .workplacePhoneNumber("010-1234-1230")
//                .workplaceDescription("사업장 설명 수정")
//                .workplaceAddress("중국")
//                .profileImage(null)
//                .imageType(null)
//                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 8, 0))
//                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 19, 0))
//                .build();
//
//        WorkplaceRequest request = new WorkplaceRequest(updatedWorkplace);
//
//        String json = objectMapper.writeValueAsString(request);
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
//                .workplacePhoneNumber("010-1234-1234")
//                .workplaceDescription("사업장 설명")
//                .workplaceAddress("대한민국")
//                .profileImage(null)
//                .imageType(null)
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
//                    .workplacePhoneNumber("010-1234-" + String.format("%04d", i))
//                    .workplaceDescription("사업장 설명 " + i)
//                    .workplaceAddress("대한민국 " + i)
//                    .profileImage(null)
//                    .imageType(null)
//                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
//                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
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
//}