package roomit.web1_2_bumblebee_be.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .cors(AbstractHttpConfigurer::disable) //cors 비활성화
                .formLogin(AbstractHttpConfigurer::disable) //form 로그인 비활성화
                // 경로별 접근
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll()); //모두 허용

        return http.build();

    }
}
