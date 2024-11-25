package roomit.main.global.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class CustomProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public CustomProviderManager(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationException lastException = null;

        for (AuthenticationProvider provider : providers) {
            if (!provider.supports(authentication.getClass())) {
                continue;
            }

            try {
                Authentication result = provider.authenticate(authentication);
                if (result != null) {
                    return result; // 인증 성공 시 반환
                }
            } catch (AuthenticationException e) {
                lastException = e; // 실패 시 예외 저장
            }
        }

        // 모든 Provider에서 인증 실패 시 마지막 예외 던지기
        if (lastException != null) {
            throw lastException;
        }

        throw new ProviderNotFoundException("No AuthenticationProvider found for " + authentication.getClass().getName());
    }
}
