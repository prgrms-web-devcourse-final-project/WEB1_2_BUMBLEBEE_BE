package roomit.main.global.oauth2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.global.config.security.util.CookieUtil;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.token.config.JWTUtil;
import roomit.main.global.token.entity.Refresh;
import roomit.main.global.token.repository.RefreshRepository;

import static roomit.main.global.common.CommonToken.*;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.redirectUrl}")
    private String oauthUrl;

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {
            CustomMemberDetails customUserDetails = (CustomMemberDetails) authentication.getPrincipal();

            String username = customUserDetails.getUsername();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            String access = jwtUtil.createJwt(JWT_ACCESS_TOKEN_NAME, username, role, JWT_ACCESS_TOKEN_EXPIRED_TIME);
            String refresh = jwtUtil.createJwt(JWT_REFRESH_TOKEN_NAME, username, role, JWT_REFRESH_TOKEN_EXPIRED_TIME);

            // Refresh Token을 쿠키에 저장
            CookieUtil.addCookie(response, JWT_REFRESH_TOKEN_NAME, refresh, JWT_COOKIE_REFRESH_TOKEN_EXPIRED_TIME);

            // Refresh Token을 Redis에 저장
            addRefreshEntity(username, refresh);

            // Redirect URL
            String redirectUrl = UriComponentsBuilder.fromHttpUrl(oauthUrl)
                    .queryParam(JWT_ACCESS_TOKEN_NAME, access)
                    .queryParam("role", role)
                    .toUriString();

            response.sendRedirect(redirectUrl);
        } catch (Exception e){
            throw ErrorCode.OAUTH_LOGIN_FAILED.commonException();
        }
    }

    private void addRefreshEntity(String username, String refreshToken) {
        Refresh refresh = new Refresh();
        refresh.changeRefresh(refreshToken);
        refresh.changeUsername(username);

        refreshRepository.save(refresh);
    }
}
