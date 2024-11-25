package roomit.web1_2_bumblebee_be.domain.token.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class BusinessAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final String password;

    public BusinessAuthenticationToken(String email, String password) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_BUSINESS")));
        this.email = email;
        this.password = password;
        setAuthenticated(false);  // 인증되지 않은 상태로 시작
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}
