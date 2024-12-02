package roomit.main.global.token.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomit.main.global.token.config.JWTUtil;
import roomit.main.global.token.service.TokenService;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        tokenService.reissue(request,response);
    }
}
