package roomit.web1_2_bumblebee_be.global.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import roomit.web1_2_bumblebee_be.domain.business.response.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.global.config.security.jwt.token.BusinessAuthenticationToken;
import roomit.web1_2_bumblebee_be.global.config.security.jwt.token.MemberAuthenticationToken;

@RequiredArgsConstructor
@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 요청 URI로 멤버와 사업자 구분
        String requestUri = request.getRequestURI();
        Authentication authenticationToken;

        if (requestUri.equals("/login/member")) {
            // 멤버 인증 토큰 생성
            authenticationToken = new MemberAuthenticationToken(email, password);
        } else if (requestUri.equals("/login/business")) {
            // 사업자 인증 토큰 생성
            authenticationToken = new BusinessAuthenticationToken(email, password);
        } else {
            throw new AuthenticationServiceException("Invalid login endpoint");
        }

        // 인증 시도
        return getAuthenticationManager().authenticate(authenticationToken);

    }

    //로그인 성공시 실행하는 메서드 (JWT발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException {
        String token;
        if (authentication.getPrincipal() instanceof CustomMemberDetails) {
            CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
            token = jwtUtil.createJwt(memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 60L);  // 멤버의 JWT
        } else if (authentication.getPrincipal() instanceof CustomBusinessDetails) {
            CustomBusinessDetails businessDetails = (CustomBusinessDetails) authentication.getPrincipal();
            token = jwtUtil.createJwt(businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 60L);  // 사업자의 JWT
        } else {
            throw new ServletException("Unknown authentication type");
        }

        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); //로그인 실패시 401 상태 코드 반환
    }
}
