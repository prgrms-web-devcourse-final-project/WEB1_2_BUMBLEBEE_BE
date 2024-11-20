package roomit.web1_2_bumblebee_be.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.exception.MemberNotFound;
import roomit.web1_2_bumblebee_be.domain.member.repository.MemberRepository;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberRegisterRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.request.MemberUpdateRequest;
import roomit.web1_2_bumblebee_be.domain.member.dto.response.MemberResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // BCryptPasswordEncoder 추가

    public void signupMember(MemberRegisterRequest memberRequest) {
        Member member = Member.builder()
                .nickName(memberRequest.getNickName())
                .age(memberRequest.getAge())
                .sex(memberRequest.getSex())
                .pwd(bCryptPasswordEncoder.encode(memberRequest.getPwd())) //비밀번호 암호화 추가
                .email(memberRequest.getEmail())
                .role(memberRequest.getRole())
                .phoneNumber(memberRequest.getPhoneNumber())
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

        member.changeAge(request.getAge());
        member.changeEmail(request.getEmail());
        member.changeRole(request.getRole());
        member.changePhoneNumber(request.getPhoneNumber());
        member.changePwd(request.getPwd());
        memberRepository.save(member);

        return new MemberResponse(member);
    }

    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }
}
