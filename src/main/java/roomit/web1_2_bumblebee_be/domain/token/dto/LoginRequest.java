package roomit.web1_2_bumblebee_be.domain.token.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
