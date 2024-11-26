package roomit.main.domain.workplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.repository.BusinessRepository;
import roomit.main.domain.business.service.BusinessService;
import roomit.main.domain.member.service.MemberService;
import roomit.main.domain.token.dto.LoginRequest;
import roomit.main.domain.token.dto.LoginResponse;
import roomit.main.domain.workplace.dto.WorkplaceRequest;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    private static String token;

    private static Long workplaceId;

    @BeforeAll
    void setUp() throws Exception {

        //회원 가입
        BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
                .businessName("테스트사업자")
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json = objectMapper.writeValueAsString(businessRegisterRequest);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated());

        //로그인
        LoginRequest loginRequest = LoginRequest.builder()
                .email("business1@gmail.com")
                .password("Business1!")
                .build();

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult loginResult = mockMvc.perform(post("/login/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue(loginResult.getResponse().getContentAsString(), LoginResponse.class);
        token = loginResponse.getToken();

        Business business = businessRepository.findByBusinessEmail("business1@gmail.com").orElseThrow(NoSuchElementException::new);

        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .business(business)
                .build();

        workplaceRepository.save(workplace);

        workplaceId = workplace.getWorkplaceId();
    }


    @Order(1)
    @Test
    @DisplayName("사업장 등록")
    void create() throws Exception {
        // Given
        WorkplaceRequest workplace = WorkplaceRequest.builder()
                .workplaceName("사업장1")
                .workplacePhoneNumber("0507-1234-5698")
                .workplaceDescription("사업장 설명1")
                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 7층")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        String json = objectMapper.writeValueAsString(workplace);

        // When
        mockMvc.perform(post("/api/v1/workplace")
                .header("Authorization", "Bearer " + token) // 토큰 추가
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

        // Then
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Order(2)
    @Test
    @DisplayName("사업장 정보")
    void getWorkplace() throws Exception {
        // Given

        // 토큰 출력 (디버깅용)
        System.out.println("Generated Token: " + token);

        // When
        mockMvc.perform(get("/api/v1/workplace/{workplaceId}", workplaceId)
                .header("Authorization", "Bearer " + token) // 토큰 추가
                .contentType(MediaType.APPLICATION_JSON)
                )

        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workplaceName").value("사업장"))
                .andDo(print());
    }

    @Order(3)
    @Test
    @DisplayName("사업장 수정")
    void update() throws Exception {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        workplaceRepository.save(workplace);

        WorkplaceRequest updatedWorkplace = WorkplaceRequest.builder()
                .workplaceName("사업장 수정")
                .workplacePhoneNumber("0507-1234-5670")
                .workplaceDescription("사업장 설명 수정")
                .workplaceAddress("서울 중구 장충단로13길 20")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 8, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 19, 0))
                .build();

        String json = objectMapper.writeValueAsString(updatedWorkplace);

        System.out.println("Generated JSON: " + json); // JSON 출력 확인

        // When
        mockMvc.perform(put("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
                        .header("Authorization", "Bearer " + token) // 토큰 추가
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
        // Then
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(4)
    @Test
    @DisplayName("사업장 삭제")
    void deleteWorkplace() throws Exception {
        // Given
        Workplace workplace = Workplace.builder()
                .workplaceName("사업장")
                .workplacePhoneNumber("0507-1234-5678")
                .workplaceDescription("사업장 설명")
                .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
                .imageUrl("http://image.url")
                .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                .build();

        workplaceRepository.save(workplace);


        // When
        mockMvc.perform(delete("/api/v1/workplace/{workplaceId}", workplace.getWorkplaceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)) // 토큰 추가
        // Then
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Order(5)
    @Test
    @DisplayName("사업장 목록 조회")
    void getWorkplaces() throws Exception {
        // Given
        for (int i = 1; i <= 100; i++) {
            Workplace workplace = Workplace.builder()
                    .workplaceName("사업장 " + i)
                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
                    .workplaceDescription("사업장 설명 " + i)
                    .workplaceAddress("서울 중구 장충단로 247 굿모닝시티 8층")
                    .imageUrl("http://image.url")
                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                    .build();

            workplaceRepository.save(workplace);
        }

        // When
        mockMvc.perform(get("/api/v1/workplace")
                        .header("Authorization", "Bearer " + token) // 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                )

        // Then
        .andExpect(status().isOk())
        .andDo(print());
    }


}