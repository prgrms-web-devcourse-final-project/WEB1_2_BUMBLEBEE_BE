package roomit.main.domain.member.service;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomit.main.domain.member.dto.request.MemberRegisterRequest;
import roomit.main.domain.member.dto.request.MemberUpdateRequest;
import roomit.main.domain.member.dto.response.MemberResponse;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.exception.MemberNotFound;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.exception.CommonException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signupMember(MemberRegisterRequest memberRequest) {
        Optional<Member> byMemberEmail =
                memberRepository.findByMemberEmail(memberRequest.email());
        boolean present = byMemberEmail.isPresent();

        if (present) {
            throw ErrorCode.MEMBER_DUPLICATE_EMAIL.commonException();
        }

        memberRepository.save(memberRequest.toEntity(bCryptPasswordEncoder));
    }

    public MemberResponse read(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        return new MemberResponse(member);
    }

    @Transactional
    public void update(Long memberId, MemberUpdateRequest request) {
        try {
            memberRepository.updateMember(request, memberId);
        } catch (Exception e){
            if(e instanceof CommonException){
                throw (CommonException)e;
            }else{
                throw ErrorCode.MEMBER_NOT_MODIFY.commonException();
            }
        }
    }

    public void delete(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        memberRepository.delete(member);
    }
}
