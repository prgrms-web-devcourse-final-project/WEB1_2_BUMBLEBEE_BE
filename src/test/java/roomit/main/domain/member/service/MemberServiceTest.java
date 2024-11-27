
package roomit.main.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.dto.request.MemberRegisterRequest;
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.dto.response.MemberResponse;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.domain.review.repository.ReviewRepository;
import roomit.main.domain.workplace.repository.WorkplaceRepository;
import roomit.main.global.error.ErrorCode;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Member member;

    private LocalDate date = LocalDate.of(2024, 11, 22);
    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        reviewRepository.deleteAll();
        workplaceRepository.deleteAll();

       member =  Member.builder()
                .birthDay(date)
                .memberSex(Sex.FEMALE)
                .memberPwd("Business1!")
                .memberEmail("sdsd@naver.com")
                .memberPhoneNumber("010-3323-2323")
                .memberNickName("치킨유저")
                .passwordEncoder(bCryptPasswordEncoder)
                .build();
    }

    @Test
    @DisplayName("멤버 등록")
    void test1(){
        MemberRegisterRequest memberRequest = MemberRegisterRequest.builder()
                .birthDay(date)
                .sex(Sex.FEMALE)
                .pwd("Business1!")
                .email("sdsd@naver.com")
                .phoneNumber("010-3323-2323")
                .nickName("치킨유저")
                .build();

        memberService.signupMember(memberRequest);

        Member member = memberRepository.findByMemberEmail("sdsd@naver.com")
                .orElseThrow(NoSuchElementException::new);



        assertTrue(bCryptPasswordEncoder.matches("Business1!",member.getMemberPwd()));
        assertEquals("치킨유저",member.getMemberNickName());

    }

    @Test
    @DisplayName("정보 조회")
    void test2(){


        memberRepository.save(member);

        MemberResponse myDate = memberService.read(member.getMemberId());

        assertEquals("2024-11-22",member.getBirthDay().toString());
        assertTrue(bCryptPasswordEncoder.matches("Business1!",myDate.pwd()));
        assertEquals("치킨유저",myDate.nickName());
    }

    @Test
    @DisplayName("없는 정보 조회")
    void test3(){


        memberRepository.save(member);
        assertThrows(ErrorCode.MEMBER_NOT_FOUND.commonException().getClass(), () -> memberService.read(member.getMemberId() + 1));


    }

    @Test
    @DisplayName(" 정보 수정")
    void test4(){


        memberRepository.save(member);

        MemberUpdateRequest memberRequest = MemberUpdateRequest.builder()
                .pwd("Business2!")
                .email("sdsd@naver.com")
                .phoneNumber("010-3323-2323")
                .nickName("이이")
                .build();



        MemberResponse myDate = memberService.update(member.getMemberId(), memberRequest);

        assertTrue(bCryptPasswordEncoder.matches("Business2!", myDate.pwd()));

    }

    @Test
    @DisplayName(" 정보 삭제")
    void test5(){


        memberRepository.save(member);

        memberService.delete(member.getMemberId());

        assertEquals(0, memberRepository.count());
    }

    @Test
    @DisplayName(" 없는 정보 삭제")
    void test6(){


        memberRepository.save(member);

        assertThrows(ErrorCode.MEMBER_NOT_FOUND.commonException().getClass(), () -> memberService.delete(member.getMemberId() + 1));

    }
}
