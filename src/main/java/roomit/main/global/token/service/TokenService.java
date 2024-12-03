package roomit.main.global.token.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import roomit.main.domain.business.dto.CustomBusinessDetails;
import roomit.main.domain.member.dto.CustomMemberDetails;
import roomit.main.domain.member.entity.Role;
import roomit.main.global.config.security.util.CookieUtil;
import roomit.main.global.error.ErrorCode;
import roomit.main.global.token.config.JWTUtil;
import roomit.main.global.token.dto.request.LoginRequest;
import roomit.main.global.token.dto.response.LoginResponse;
import roomit.main.global.token.entity.Refresh;
import roomit.main.global.token.repository.RefreshRepository;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshRepository refreshRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginResponse memberLogin(LoginRequest loginRequest, HttpServletResponse response){
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), Collections.emptyList());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
            String username = authentication.getName();

            // Access Token 생성
            String accessToken = jwtUtil.createJwt("access", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 15L); // 15분 유효
            // Refresh Token 생성
            String refreshToken = jwtUtil.createJwt("refresh", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 60 * 24L); // 24시간 유효

            // Access Token은 헤더에 추가
            response.addHeader("Authorization", accessToken);

            // Refresh Token을 쿠키에 저장
            CookieUtil.addCookie(response, "refresh", refreshToken, 60 * 60 * 24); // 1일 유효

            // Refresh Token을 Redis에 저장
            addRefreshEntity(username, refreshToken);

            return new LoginResponse(Role.ROLE_USER);

        } catch (Exception e) {
            throw ErrorCode.LOGIN_FAILED.commonException();
        }
    }

    public LoginResponse businessLogin(LoginRequest loginRequest, HttpServletResponse response){
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), Collections.emptyList());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomBusinessDetails businessDetails = (CustomBusinessDetails) authentication.getPrincipal();
            String username = authentication.getName();

            // Access Token 생성
            String accessToken = jwtUtil.createJwt("access", businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 15L); // 15분 유효
            // Refresh Token 생성
            String refreshToken = jwtUtil.createJwt("refresh", businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 60 * 24L); // 24시간 유효

            // Access Token은 헤더에 추가
            response.addHeader("Authorization", accessToken);

            // Refresh Token을 쿠키에 저장
            CookieUtil.addCookie(response, "refresh", refreshToken, 60 * 60 * 24); // 1일 유효

            // Refresh Token을 Redis에 저장
            addRefreshEntity(username, refreshToken);

            return new LoginResponse(Role.ROLE_BUSINESS);

        } catch (Exception e) {
            throw ErrorCode.LOGIN_FAILED.commonException();
        }
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response){

        String refresh = extractRefreshToken(request);

        if (refresh == null) {
            throw ErrorCode.MISSING_TOKEN.commonException();
        }

        //만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            throw ErrorCode.EXPIRED_TOKEN.commonException();
        }

        // 토큰이 refresh인지 체크
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            throw ErrorCode.MALFORMED_TOKEN.commonException();
        }

        // Redis에 토큰 존재 여부 확인
        if (!refreshRepository.existsById(refresh)) {
            throw ErrorCode.INVALID_TOKEN.commonException();
        }

        String username = jwtUtil.getUsername(refresh);
        String  role = jwtUtil.getRole(refresh).name();

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 1000 * 60 * 15L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 1000 * 60 * 60 * 24L);

        // Redis에서 기존 Refresh Token 삭제 및 새로 저장
        refreshRepository.deleteById(refresh);
        addRefreshEntity(username, newRefresh);

        //response
        response.addHeader("Authorization", newAccess);
        CookieUtil.addCookie(response, "refresh", newRefresh, 60 * 60 * 24); // 1일 유효
    }


    private void addRefreshEntity(String username, String refreshToken) {
        Refresh refresh = new Refresh();
        refresh.changeRefresh(refreshToken);
        refresh.changeUsername(username);

        refreshRepository.save(refresh);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
