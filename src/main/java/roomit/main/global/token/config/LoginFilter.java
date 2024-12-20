package roomit.main.global.token.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import roomit.main.global.token.repository.RefreshRepository;
import roomit.main.global.token.token.BusinessAuthenticationToken;
import roomit.main.global.token.token.MemberAuthenticationToken;

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
}
