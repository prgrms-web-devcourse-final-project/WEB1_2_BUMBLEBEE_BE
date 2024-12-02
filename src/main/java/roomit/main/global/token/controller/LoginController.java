package roomit.main.global.token.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.token.dto.request.LoginRequest;
import roomit.main.global.token.dto.response.LoginResponse;
import roomit.main.global.token.service.TokenService;


@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login/member")
    public LoginResponse memberLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return tokenService.memberLogin(loginRequest,response);
    }

    @PostMapping("/login/business")
    public LoginResponse businessLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return tokenService.businessLogin(loginRequest,response);
    }
}
