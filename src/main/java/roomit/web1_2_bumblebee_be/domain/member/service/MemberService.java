package roomit.web1_2_bumblebee_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.MemberResponse;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signupMember(MemberRegisterRequest memberRequest) {
        Member member = Member.builder()
                .birthDay(memberRequest.birthDay())
                .memberSex(memberRequest.sex())
                .memberPwd(memberRequest.pwd()) //암호화
                .memberEmail(memberRequest.email())
                .memberPhoneNumber(memberRequest.phoneNumber())
                .memberNickName(memberRequest.nickName())
                .passwordEncoder(bCryptPasswordEncoder)
                .build();

        memberRepository.save(member);
    }

    public MemberResponse read(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        return new MemberResponse(member);
    }

    public MemberResponse update(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        try {
            member.changeEmail(request.email());
            member.changeNickName(request.nickName());
            member.changePhoneNumber(request.phoneNumber());
            member.changePwd(request.pwd());
            memberRepository.save(member);

        }catch (Exception e){
            throw ErrorCode.MEMBER_UPDATE_EXCEPTION.commonException();
        }

        return new MemberResponse(member);
    }

    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        memberRepository.delete(member);
    }
}
