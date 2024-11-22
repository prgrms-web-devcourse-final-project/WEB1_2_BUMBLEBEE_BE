package roomit.web1_2_bumblebee_be.global.config.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import roomit.web1_2_bumblebee_be.global.config.security.jwt.JWTUtil;
import roomit.web1_2_bumblebee_be.global.config.security.jwt.dto.LoginRequest;
import roomit.web1_2_bumblebee_be.global.config.security.jwt.dto.LoginResponse;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Log4j2
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/member")
    public ResponseEntity<?> memberLogin(@RequestBody LoginRequest loginRequest) {
        log.info(loginRequest.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();

            // JWT 생성
            String jwt = jwtUtil.createJwt(memberDetails.getUsername(), "ROLE_USER", 1000 * 60 * 60L);
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/business")
    public ResponseEntity<?> businessLogin(@RequestBody LoginRequest loginRequest) {
        log.info(loginRequest.getEmail());
        log.info(loginRequest.getPassword());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            CustomBusinessDetails businessDetails = (CustomBusinessDetails) authentication.getPrincipal();

            // JWT 생성
            String jwt = jwtUtil.createJwt(businessDetails.getUsername(), "ROLE_BUSINESS", 1000 * 60 * 60L);
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
