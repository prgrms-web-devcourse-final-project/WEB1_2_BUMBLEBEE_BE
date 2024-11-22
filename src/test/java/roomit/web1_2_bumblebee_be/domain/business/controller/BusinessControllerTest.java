package roomit.web1_2_bumblebee_be.domain.business.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import roomit.web1_2_bumblebee_be.domain.business.repository.BusinessRepository;
import roomit.web1_2_bumblebee_be.domain.business.dto.request.BusinessRegisterRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessRepository businessRepository;


    @Test
    @Order(1)
    @DisplayName("사업자 회원가입")
    void test() throws Exception{
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
                .andExpect(status().isOk())
                .andExpect(content().string("사업자 회원 등록이 완료되었습니다."))
                .andDo(print());
    }

    @Test
    @Order(2)
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


}
