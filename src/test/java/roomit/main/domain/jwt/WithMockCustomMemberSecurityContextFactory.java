package roomit.main.domain.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;
import roomit.main.domain.jwt.mock.WithMockCustomMember;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.repository.MemberRepository;
import roomit.main.global.error.ErrorCode;

@Component
public class WithMockCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember customMember) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // @BeforeEach로 멤버 생성해도 찾을 수 없는 에러 발생
        Member member = memberRepository.findByMemberEmail("member@aaa.com").orElseThrow(ErrorCode.MEMBER_NOT_FOUND::commonException);

        CustomMemberDetails memberDetails = new CustomMemberDetails(member);
        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails,null,memberDetails.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
