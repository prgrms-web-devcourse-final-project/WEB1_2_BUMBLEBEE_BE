package roomit.web1_2_bumblebee_be.domain.token.config;

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
import roomit.web1_2_bumblebee_be.domain.business.dto.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.token.entity.RefreshEntity;
import roomit.web1_2_bumblebee_be.domain.token.repository.RefreshRepository;
import roomit.web1_2_bumblebee_be.domain.token.token.BusinessAuthenticationToken;
import roomit.web1_2_bumblebee_be.domain.token.token.MemberAuthenticationToken;
import roomit.web1_2_bumblebee_be.global.config.security.util.CookieUtil;

import java.util.Date;

@RequiredArgsConstructor
@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException {
        String accessToken;
        String refreshToken;
        String username = authentication.getName();

        if (authentication.getPrincipal() instanceof CustomMemberDetails) {
            CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
            accessToken = jwtUtil.createJwt("access", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 15L); // 15분 유효한 Access Token
            refreshToken = jwtUtil.createJwt("refresh", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 60 * 24L); // 24시간 유효한 Refresh Token
        } else if (authentication.getPrincipal() instanceof CustomBusinessDetails) {
            CustomBusinessDetails businessDetails = (CustomBusinessDetails) authentication.getPrincipal();
            accessToken = jwtUtil.createJwt("access", businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 15L); // 15분 유효한 Access Token
            refreshToken = jwtUtil.createJwt("refresh", businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 60 * 24L); // 24시간 유효한 Refresh Token
        } else {
            throw new ServletException("Unknown authentication type");
        }

        // Access Token은 헤더에 추가
        response.addHeader("Authorization", "Bearer " + accessToken);

        // Refresh Token은 쿠키에 추가
        CookieUtil.addCookie(response, "refreshToken", refreshToken, 60 * 60 * 24); // 1일 유효

        addRefreshEntity(username,refreshToken,6000000L);
    }

    //로그인 실패시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); //로그인 실패시 401 상태 코드 반환
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }


}
