package roomit.main.domain.token.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
