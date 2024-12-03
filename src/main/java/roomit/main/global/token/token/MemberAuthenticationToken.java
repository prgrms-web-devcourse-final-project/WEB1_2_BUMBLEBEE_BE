package roomit.main.global.token.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class MemberAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final String password;

    public MemberAuthenticationToken(String email, String password) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
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
