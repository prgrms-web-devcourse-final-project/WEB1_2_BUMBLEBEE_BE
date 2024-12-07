//
//package roomit.main.domain.member.controller;
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
//import java.time.LocalDate;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import roomit.main.domain.member.dto.request.MemberRegisterRequest;
//import roomit.main.domain.member.dto.request.MemberUpdateRequest;
//import roomit.main.domain.member.entity.Member;
//import roomit.main.domain.member.entity.Sex;
//import roomit.main.domain.member.repository.MemberRepository;
//import roomit.main.global.error.ErrorCode;
//import roomit.main.global.token.dto.request.LoginRequest;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@ActiveProfiles("test")
//class MemberControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    private LocalDate date;
//    private Member member;
//
//    @BeforeEach
//    void setUp() {
//        memberRepository.deleteAll();
//        date = LocalDate.of(2024, 11, 22);
//        member = Member.builder()
//                .birthDay(date)
//                .memberSex(Sex.FEMALE)
//                .memberPwd("Business1!")
//                .memberEmail("sdsd@naver.com")
//                .memberPhoneNumber("010-3323-2323")
//                .memberNickName("치킨유저")
//                .passwordEncoder(bCryptPasswordEncoder)
//                .build();
//    }
//
//    @Test
//    @DisplayName("성별, 비밀번호 없을떄 등록시 필요한 검증값 나오게끔 테스트")
//    void test() throws Exception {
//
//
//        MemberRegisterRequest 치킨유저 = MemberRegisterRequest.builder()
//                .birthDay(date)
//                .pwd("")
//                .email("sdsd@naver.com")
//                .phoneNumber("010-3323-2323")
//                .nickName("치킨유저")
//                .build();
//
//        String json = objectMapper.writeValueAsString(치킨유저);
//
//        mockMvc.perform(post("/api/v1/member/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("회원 등록")
//    void test1() throws Exception {
//        MemberRegisterRequest memberRequest = MemberRegisterRequest.builder()
//                .birthDay(date)
//                .sex(Sex.FEMALE)
//                .pwd("Business1!")
//                .email("sdsd@naver.com")
//                .phoneNumber("010-3323-2323")
//                .nickName("치킨유저")
//                .build();
//
//
//        String json = objectMapper.writeValueAsString(memberRequest);
//
//        mockMvc.perform(post("/api/v1/member/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("회원 정보")
//    void test2() throws Exception {
//
//        memberRepository.save(member);
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                        .build();
//
//        String json = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String token = loginResult.getResponse().getHeader("Authorization");
//
//
//
//        mockMvc.perform(get("/api/v1/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.birthDay").value("2024-11-22"))
//                .andExpect(jsonPath("$.sex").value(Sex.FEMALE.name()))
//                .andExpect(jsonPath("$.email").value("sdsd@naver.com"))
//                .andExpect(jsonPath("$.phoneNumber").value("010-3323-2323"))
//                .andExpect(jsonPath("$.nickName").value("치킨유저"))
//                .andDo(print());
//
//
//        Member member1 = memberRepository.findByMemberEmail("sdsd@naver.com")
//                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);
//
//        Assertions.assertTrue(bCryptPasswordEncoder.matches("Business1!", member1.getMemberPwd()));
//    }
//
//    @Test
//    @DisplayName("회원 정보 수정")
//    void test3() throws Exception {
//
//
//        memberRepository.save(member);
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String loginJson = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String token = loginResult.getResponse().getHeader("Authorization");
//
//
//        MemberUpdateRequest memberRequest = MemberUpdateRequest.builder()
//                .sex(Sex.MALE)
//                .email("sdsd@naver.com")
//                .phoneNumber("010-3323-2323")
//                .nickName("이이")
//                .birthDay(LocalDate.parse("1999-01-01"))
//                .build();
//
//        String json = objectMapper.writeValueAsString(memberRequest);
//
//        mockMvc.perform(put("/api/v1/member")
//                        .content(json)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(status().isNoContent())
//                .andExpect(jsonPath("$.email").value("sdsd@naver.com"))
//                .andExpect(jsonPath("$.phoneNumber").value("010-3323-2323"))
//                .andExpect(jsonPath("$.nickName").value("이이"))
//                .andDo(print());
//
//        Member member1 = memberRepository.findByMemberEmail("sdsd@naver.com")
//                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);
//
//    }
//
//    @Test
//    @DisplayName("회원 정보 삭제")
//    void test4() throws Exception {
//
//
//        memberRepository.save(member);
//        LoginRequest loginRequest = LoginRequest.builder()
//                .email("sdsd@naver.com")
//                .password("Business1!")
//                .build();
//
//        String loginJson = objectMapper.writeValueAsString(loginRequest);
//
//        MvcResult loginResult = mockMvc.perform(post("/login/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loginJson))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String token = loginResult.getResponse().getHeader("Authorization");
//
//
//        mockMvc.perform(delete("/api/v1/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + token)
//                )
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        Assertions.assertEquals(0, memberRepository.count());
//    }
//}
