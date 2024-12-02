package roomit.main.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import roomit.main.domain.member.dto.request.MemberRegisterRequest;
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.dto.response.MemberResponse;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.global.oauth2.dto.PROVIDER;
import roomit.main.global.error.ErrorCode;

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
                .provider(PROVIDER.BASIC)
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
            member.changeSex(request.sex());
            member.changeBirthDay(request.birthDay());
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
