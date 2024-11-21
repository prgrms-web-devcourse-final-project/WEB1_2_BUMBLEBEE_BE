package roomit.web1_2_bumblebee_be.global.config.security.jwt.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
