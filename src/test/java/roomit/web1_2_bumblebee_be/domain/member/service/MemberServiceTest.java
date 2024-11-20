package roomit.web1_2_bumblebee_be.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;
import roomit.web1_2_bumblebee_be.domain.member.entity.Sex;
import roomit.web1_2_bumblebee_be.domain.member.exception.MemberNotFound;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.MemberResponse;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("멤버 등록")
    void test1(){
        MemberRegisterRequest memberRequest = MemberRegisterRequest.builder()
                .age(Age.TEN)
                .sex(Sex.FEMALE)
                .pwd("1111")
                .email("이시현@Naver.com")
                .role(Role.Admin)
                .phoneNumber("010-33230-23")
                .nickName("치킨유저")
                .build();

        memberService.signupMember(memberRequest);

        Member member = memberRepository.findByMemberEmail("이시현@Naver.com")
                .orElseThrow(NoSuchElementException::new);



        assertEquals("1111",member.getMemberPwd());
        assertEquals("치킨유저",member.getMemberNickName());

    }

    @Test
    @DisplayName("정보 조회")
    void test2(){
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

        MemberResponse myDate = memberService.read(member.getMemberId());

        assertEquals("10대",member.getMemberAge().getDescription());
        assertEquals("1111",myDate.getPwd());

    }

    @Test
    @DisplayName("없는 정보 조회")
    void test3(){
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
        MemberNotFound memberNotFound = new MemberNotFound();
        System.out.println(new MemberNotFound());
        assertThrows( MemberNotFound.class, () -> memberService.read(member.getMemberId() + 1));


    }

    @Test
    @DisplayName(" 정보 수정")
    void test4(){
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
                .pwd("1111")
                .email("이시현@Naver.com")
                .phoneNumber("010-33230-23")
                .memberNickName("이이")
                .build();



        MemberResponse myDate = memberService.update(member.getMemberId(), memberRequest);

        assertEquals("1111",myDate.getPwd());

    }

    @Test
    @DisplayName(" 정보 삭제")
    void test5(){
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

        memberService.delete(member.getMemberId());

        assertEquals(0, memberRepository.count());
    }

    @Test
    @DisplayName(" 없는 정보 삭제")
    void test6(){
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

        assertThrows(MemberNotFound.class, () -> memberService.delete(member.getMemberId() + 1));

    }
}