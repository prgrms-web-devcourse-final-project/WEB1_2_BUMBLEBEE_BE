package roomit.web1_2_bumblebee_be.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import roomit.web1_2_bumblebee_be.domain.member.entity.Age;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.member.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.request.MemberUpdateRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 등록")
    void test1() throws Exception{
        MemberRegisterRequest memberRequest = MemberRegisterRequest.builder()
                .age(Age.TEN)
                .sex(Sex.FEMALE)
                .pwd("1111")
                .email("이시현@Naver.com")
                .role(Role.Admin)
                .phoneNumber("010-33230-23")
                .nickName("치킨유저")
                .build();


        String json = objectMapper.writeValueAsString(memberRequest);

        mockMvc.perform(post("/api/v1/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보")
    void test2() throws Exception{
        Member member = Member.builder()
                .memberAge(Age.TEN)
                .memberSex(Sex.FEMALE)
                .memberPwd("1111")
                .memberEmail("이시현@Naver.com")
                .memberRole(Role.Admin)
                .memberPhoneNumber("010-33230-23")
                .memberNickName("치킨유저")
                .build();

        memberRepository.save(member);

        mockMvc.perform(get("/api/v1/member/{memberId}", member.getMemberId())
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(Age.TEN.name()))
                .andExpect(jsonPath("$.sex").value(Sex.FEMALE.name()))
                .andExpect(jsonPath("$.pwd").value("1111"))
                .andExpect(jsonPath("$.email").value("이시현@Naver.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-33230-23"))
                .andExpect(jsonPath("$.nickName").value("치킨유저"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보 수정")
    void test3() throws Exception{
        Member member = Member.builder()
                .memberAge(Age.TEN)
                .memberSex(Sex.FEMALE)
                .memberPwd("1111")
                .memberEmail("이시현@Naver.com")
                .memberRole(Role.Admin)
                .memberPhoneNumber("010-33230-23")
                .memberNickName("치킨유저")
                .build();

        memberRepository.save(member);


        MemberUpdateRequest memberRequest = MemberUpdateRequest.builder()
                .pwd("2222")
                .email("김시현@Naver.com")
                .phoneNumber("010-33230-23")
                .memberNickName("이이")
                .build();

        String json = objectMapper.writeValueAsString(memberRequest);

        mockMvc.perform(put("/api/v1/member/{memberId}", member.getMemberId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pwd").value("2222"))
                .andExpect(jsonPath("$.email").value("김시현@Naver.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-33230-23"))
                .andExpect(jsonPath("$.nickName").value("이이"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보 삭제")
    void test4() throws Exception{
        Member member = Member.builder()
                .memberAge(Age.TEN)
                .memberSex(Sex.FEMALE)
                .memberPwd("1111")
                .memberEmail("이시현@Naver.com")
                .memberRole(Role.Admin)
                .memberPhoneNumber("010-33230-23")
                .memberNickName("치킨유저")
                .build();

        memberRepository.save(member);



        mockMvc.perform(delete("/api/v1/member/{memberId}", member.getMemberId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(0, memberRepository.count());
    }
}