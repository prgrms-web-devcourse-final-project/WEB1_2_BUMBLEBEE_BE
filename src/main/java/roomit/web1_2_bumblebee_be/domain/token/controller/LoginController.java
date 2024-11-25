package roomit.web1_2_bumblebee_be.domain.token.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomit.web1_2_bumblebee_be.domain.business.dto.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.token.config.JWTUtil;
import roomit.web1_2_bumblebee_be.domain.token.dto.LoginRequest;
import roomit.web1_2_bumblebee_be.domain.token.dto.LoginResponse;
import roomit.web1_2_bumblebee_be.domain.token.entity.RefreshEntity;
import roomit.web1_2_bumblebee_be.domain.token.repository.RefreshRepository;
import roomit.web1_2_bumblebee_be.global.config.security.util.CookieUtil;

import java.util.Date;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/member")
    public ResponseEntity<?> memberLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
            String username = authentication.getName();

            // Access Token 생성
            String accessToken = jwtUtil.createJwt("access", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 15L); // 15분 유효
            // Refresh Token 생성
            String refreshToken = jwtUtil.createJwt("refresh", memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 60 * 24L); // 24시간 유효

            // Access Token은 헤더에 추가
            response.addHeader("Authorization", "Bearer " + accessToken);

            // Refresh Token을 쿠키에 저장
            CookieUtil.addCookie(response, "refresh", refreshToken, 60 * 60 * 24); // 1일 유효

            // Refresh Token을 DB에 저장
            addRefreshEntity(username,refreshToken,6000000L);

            // Access Token을 Body로 반환
            return ResponseEntity.ok(new LoginResponse(accessToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/business")
    public ResponseEntity<?> businessLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomBusinessDetails businessDetails = (CustomBusinessDetails) authentication.getPrincipal();
            String username = authentication.getName();

            // Access Token 생성
            String accessToken = jwtUtil.createJwt("access", businessDetails.getUsername(), "ROLE_BUSINESS", 60 * 1000L); // 60초 유효
            // Refresh Token 생성
            String refreshToken = jwtUtil.createJwt("refresh", businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 60 * 24L); // 24시간 유효

            // Access Token은 헤더에 추가
            response.addHeader("Authorization", "Bearer " + accessToken);

            // Refresh Token을 쿠키에 저장
            CookieUtil.addCookie(response, "refresh", refreshToken, 60 * 60 * 24); // 1일 유효

            // Refresh Token을 DB에 저장
            addRefreshEntity(username,refreshToken,6000000L);

            // Access Token을 Body로 반환
            return ResponseEntity.ok(new LoginResponse(accessToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
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
