package roomit.main.domain.business.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import roomit.main.domain.business.dto.request.BusinessRegisterRequest;
import roomit.main.domain.business.dto.request.BusinessUpdateRequest;
import roomit.main.global.token.dto.request.LoginRequest;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 인스턴스를 하나만 생성하도록 설정
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    @BeforeAll
    void setUp() throws Exception {
      //회원 가입
      BusinessRegisterRequest businessRegisterRequest = BusinessRegisterRequest.builder()
          .businessName("테스트사업자")
          .businessEmail("business1@gmail.com")
          .businessPwd("Business1!")
          .businessNum("123-99-19347")
          .build();

      String json = objectMapper.writeValueAsString(businessRegisterRequest);

      mockMvc.perform(post("/api/v1/business/signup")
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
          )
          .andExpect(status().isCreated());

      /// 로그인
      LoginRequest loginRequest = LoginRequest.builder()
          .email("business1@gmail.com")
          .password("Business1!")
          .build();

      String loginJson = objectMapper.writeValueAsString(loginRequest);

      // 로그인 요청을 보내고 응답 받기
      MvcResult loginResult = mockMvc.perform(post("/login/business")
              .contentType(MediaType.APPLICATION_JSON)
              .content(loginJson))
          .andExpect(status().isOk())
          .andReturn();

      // 응답 헤더에서 Authorization 토큰을 추출
      token = loginResult.getResponse().getHeader("Authorization");

      if (token == null) {
        System.out.println("Authorization header is missing or incorrect.");
      }
    }

    @Test
    @Order(1)
    @DisplayName("Validation 처리 테스트 - BusinessName 필수 입력 및 길이 제한")
    void test1() throws Exception {
        // 1. 사업자 이름이 빈 값일 때
        BusinessRegisterRequest request1 = BusinessRegisterRequest.builder()
                .businessName("") // 유효하지 않은 값 <빈 값>
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json1 = objectMapper.writeValueAsString(request1);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
                .andExpect(jsonPath("$.validation.businessName").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다.")) // Validation 메시지 검증
                .andDo(print());


        // 2. 사업자 이름이 너무 짧을 때
        BusinessRegisterRequest request2 = BusinessRegisterRequest.builder()
                .businessName("A") // 유효하지 않은 값 <짧은 값>
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json2 = objectMapper.writeValueAsString(request2);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
                .andExpect(jsonPath("$.validation.businessName").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다.")) // Validation 메시지 검증
                .andDo(print());

        // 3. 사업자 이름이 너무 길 때
        BusinessRegisterRequest request3 = BusinessRegisterRequest.builder()
                .businessName("AAAAAAAAAAA") // 유효하지 않은 값 <너무 긴 값>
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json3 = objectMapper.writeValueAsString(request3);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
                .andExpect(jsonPath("$.validation.businessName").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다.")) // Validation 메시지 검증
                .andDo(print());

        // 4. 사업자 이름에 특수 문자가 포함될 때
        BusinessRegisterRequest request4 = BusinessRegisterRequest.builder()
                .businessName("사업자!!!") // 유효하지 않은 값 <특수 문자 포함>
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json4 = objectMapper.writeValueAsString(request4);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json4))
                .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
                .andExpect(jsonPath("$.validation.businessName").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다.")) // Validation 메시지 검증
                .andDo(print());

        // 5. 사업자 이름에 공백 포함될 때
        BusinessRegisterRequest request5 = BusinessRegisterRequest.builder()
                .businessName("공백 테스트") // 유효하지 않은 값 <특수 문자 포함>
                .businessEmail("business1@gmail.com")
                .businessPwd("Business1!")
                .businessNum("123-12-12347")
                .build();

        String json5 = objectMapper.writeValueAsString(request5);

        mockMvc.perform(post("/api/v1/business/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json5))
                .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
                .andExpect(jsonPath("$.validation.businessName").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다.")) // Validation 메시지 검증
                .andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("Validation 처리 테스트 - BusinessEmail 필수 입력 및 길이 제한")
    void test2() throws Exception {
    // 1. 사업자 이메일이 빈 값일 때
    BusinessRegisterRequest request1 = BusinessRegisterRequest.builder()
        .businessName("테스트사업자")
        .businessEmail("") // 유효하지 않은 값 <빈 값>
        .businessPwd("Business1!")
        .businessNum("123-12-12347")
        .build();

    String json1 = objectMapper.writeValueAsString(request1);

    mockMvc.perform(post("/api/v1/business/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json1))
        .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
        .andExpect(jsonPath("$.validation.businessEmail").value("이메일 형식이 올바르지 않습니다.")) // Validation 메시지 검증
        .andDo(print());


    // 2. 사업자 이메일에 @가 없을 때
    BusinessRegisterRequest request2 = BusinessRegisterRequest.builder()
        .businessName("테스트사업자")
        .businessEmail("business1.gmail.com") // 유효하지 않은 값 <@제외>
        .businessPwd("Business1!")
        .businessNum("123-12-12347")
        .build();

    String json2 = objectMapper.writeValueAsString(request2);

    mockMvc.perform(post("/api/v1/business/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json2))
        .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
        .andExpect(jsonPath("$.validation.businessEmail").value("이메일 형식이 올바르지 않습니다.")) // Validation 메시지 검증
        .andDo(print());

    // 3. 사업자 이메일에 . 이 없을 때
    BusinessRegisterRequest request3 = BusinessRegisterRequest.builder()
        .businessName("테스트사업자")
        .businessEmail("business1@gmailcom") // 유효하지 않은 값 <.이 없는 경우>
        .businessPwd("Business1!")
        .businessNum("123-12-12347")
        .build();

    String json3 = objectMapper.writeValueAsString(request3);

    mockMvc.perform(post("/api/v1/business/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json3))
        .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
        .andExpect(jsonPath("$.validation.businessEmail").value("이메일 형식이 올바르지 않습니다.")) // Validation 메시지 검증
        .andDo(print());

    // 4. 사업자 이메일에 @ . 이외의 특수문자 포함될 때
    BusinessRegisterRequest request4 = BusinessRegisterRequest.builder()
        .businessName("테스트사업자")
        .businessEmail("business1@gmail!.com") // 유효하지 않은 값 <@, . 이외의 특수문자>
        .businessPwd("Business1!")
        .businessNum("123-12-12347")
        .build();

    String json4 = objectMapper.writeValueAsString(request4);

    mockMvc.perform(post("/api/v1/business/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json4))
        .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
        .andExpect(jsonPath("$.validation.businessEmail").value("이메일 형식이 올바르지 않습니다.")) // Validation 메시지 검증
        .andDo(print());

    // 5. 사업자 이메일에 공백이 포함될 때
    BusinessRegisterRequest request5 = BusinessRegisterRequest.builder()
        .businessName("테스트사업자")
        .businessEmail("business1@gmail. com") // 유효하지 않은 값 <공백>
        .businessPwd("Business1!")
        .businessNum("123-12-12347")
        .build();

    String json5 = objectMapper.writeValueAsString(request5);

    mockMvc.perform(post("/api/v1/business/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json5))
        .andExpect(jsonPath("$.code").value("400")) // 코드가 "400"인지 검증
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // 메시지가 "잘못된 요청입니다."인지 검증
        .andExpect(jsonPath("$.validation.businessEmail").value("이메일 형식이 올바르지 않습니다.")) // Validation 메시지 검증
        .andDo(print());
  }

    @Test
    @Order(3)
    @DisplayName("사업자 정보 조회")
    void test3() throws Exception {
      System.out.println(token);
    mockMvc.perform(get("/api/v1/business")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.businessName").value("테스트사업자")) // businessName 값 검증
        .andExpect(jsonPath("$.businessNum").value("123-99-19347")) // businessNum 값 검증
        .andExpect(jsonPath("$.businessEmail").value("business1@gmail.com")); // businessEmail 값 검증

  }

    @Test
    @Order(4)
    @DisplayName("사업자 정보 수정")
    void test4() throws Exception {
      BusinessUpdateRequest businessUpdateRequest = BusinessUpdateRequest.builder()
          .businessEmail("businessModify@gmail.com")
          .businessName("ModifyTest")
          .businessNum("999-99-99999")
          .build();

      String businessModify = objectMapper.writeValueAsString(businessUpdateRequest);

      mockMvc.perform(put("/api/v1/business")
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", "Bearer " + token)
              .content(businessModify))
          .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    @DisplayName("사업자 탈퇴")
    void test5() throws Exception {
    //로그인
    LoginRequest loginRequest = LoginRequest.builder()
        .email("businessModify@gmail.com")
        .password("Business1!")
        .build();

    String loginJson = objectMapper.writeValueAsString(loginRequest);

    MvcResult loginResult = mockMvc.perform(post("/login/business")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginJson))
        .andExpect(status().isOk())
        .andReturn();

      token = loginResult.getResponse().getHeader("Authorization");

    mockMvc.perform(delete("/api/v1/business")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isNoContent());
  }

}
