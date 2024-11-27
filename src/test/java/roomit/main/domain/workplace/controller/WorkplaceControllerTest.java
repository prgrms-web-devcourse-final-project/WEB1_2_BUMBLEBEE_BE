package roomit.main.domain.workplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import roomit.main.domain.token.dto.LoginRequest;
import roomit.main.domain.token.dto.LoginResponse;
import roomit.main.domain.workplace.dto.WorkplaceRequest;
import roomit.main.domain.workplace.entity.Workplace;
import roomit.main.domain.workplace.repository.WorkplaceRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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

    private Business business;

    @BeforeAll
    void setUp() throws Exception {
        businessRepository.deleteAll();
        workplaceRepository.deleteAll();

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

        business = businessRepository.findByBusinessEmail("business1@gmail.com").orElseThrow(NoSuchElementException::new);

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
        workplaceRepository.deleteAll();
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
                .andExpect(jsonPath("$.length()").value(100))
                .andExpect(jsonPath("$[0].workplaceName").value("사업장 1"))
                .andExpect(jsonPath("$[1].workplaceName").value("사업장 2"))
                .andDo(print());
    }

    @Order(6)
    @Test
    @DisplayName("사업자 ID로 목록 조회")
    void getWorkplacesByBusinessId() throws Exception {
        // Given
        workplaceRepository.deleteAll();
        List<String> addresses = Arrays.asList(
                "서울특별시 중구 세종대로 110 서울시청",
                "서울특별시 중구 을지로 30 시그니처타워",
                "서울특별시 용산구 이태원로 29 서울드래곤시티",
                "서울특별시 강남구 테헤란로 152 강남파이낸스센터",
                "서울특별시 영등포구 국제금융로 10 서울국제금융센터",
                "서울특별시 송파구 올림픽로 300 롯데월드타워",
                "서울특별시 마포구 월드컵북로 396 상암동 DMC타워",
                "서울특별시 강서구 마곡중앙로 161-8 LG사이언스파크",
                "서울특별시 성동구 아차산로 113 성수동 헤이그라운드",
                "서울특별시 서초구 서초대로 411 GT타워"
        );


        for (int i = 1; i <= 10; i++) {
            Workplace workplace = Workplace.builder()
                    .workplaceName("사업장 " + i)
                    .workplacePhoneNumber("0507-1234-" + String.format("%04d", i))
                    .workplaceDescription("사업장 설명 " + i)
                    .workplaceAddress(addresses.get(i-1))
                    .imageUrl("http://image.url")
                    .workplaceStartTime(LocalDateTime.of(2023, 1, 1, 9, 0))
                    .workplaceEndTime(LocalDateTime.of(2023, 1, 1, 18, 0))
                    .business(business)
                    .build();

            Workplace save = workplaceRepository.save(workplace);
        }

        // When
        mockMvc.perform(get("/api/v1/business/workplace")
                        .header("Authorization", "Bearer " + token) // 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                )

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].workplaceName").value("사업장 1"))
                .andDo(print());
    }

}