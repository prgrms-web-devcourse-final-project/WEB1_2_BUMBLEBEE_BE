package roomit.main.global.oauth2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Role;
import roomit.main.global.config.security.util.CookieUtil;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.token.config.JWTUtil;
import roomit.main.global.token.dto.response.LoginResponse;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.redirectUrl}")
    private String oauthUrl;


    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {
            CustomMemberDetails customUserDetails = (CustomMemberDetails) authentication.getPrincipal();

            String username = customUserDetails.getUsername();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            String access = jwtUtil.createJwt("access", username, role, 1000 * 60 * 15L);
            String refresh = jwtUtil.createJwt("refresh", username, role, 1000 * 60 * 60 * 24L);

            CookieUtil.addCookie(response, "refresh", refresh, 60 * 60);

            log.info(role);

            // Redirect URL
            String redirectUrl = oauthUrl + access +"?role="+role;

            response.sendRedirect(redirectUrl);
        } catch (Exception e){
            throw ErrorCode.OAUTH_LOGIN_FAILED.commonException();
        }
    }
}
