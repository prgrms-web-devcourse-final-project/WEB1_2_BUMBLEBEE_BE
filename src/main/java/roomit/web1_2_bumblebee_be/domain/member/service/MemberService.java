package roomit.web1_2_bumblebee_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.MemberResponse;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.exception.MemberNotFound;
import roomit.web1_2_bumblebee_be.domain.member.exception.MemberUpdateException;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signupMember(MemberRegisterRequest memberRequest) {
        log.info(memberRequest.getNickName());
        Member member = Member.builder()
                .memberAge(memberRequest.getAge())
                .memberSex(memberRequest.getSex())
                .memberPwd(bCryptPasswordEncoder.encode(memberRequest.getPwd())) //암호화
                .memberEmail(memberRequest.getEmail())
                .memberPhoneNumber(memberRequest.getPhoneNumber())
                .memberNickName(memberRequest.getNickName())
                .build();

        memberRepository.save(member);
    }

    public MemberResponse read(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return new MemberResponse(member);
    }

    public MemberResponse update(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        try {
            member.changeEmail(request.getEmail());
            member.changeNickName(request.getMemberNickName());
            member.changePhoneNumber(request.getPhoneNumber());
            member.changePwd(request.getPwd());
            memberRepository.save(member);

        }catch (MemberUpdateException e){
            throw new MemberUpdateException();
        }

        return new MemberResponse(member);
    }

    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }
}
