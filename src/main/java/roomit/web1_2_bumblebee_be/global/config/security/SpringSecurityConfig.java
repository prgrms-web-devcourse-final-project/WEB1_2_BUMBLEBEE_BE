package roomit.web1_2_bumblebee_be.global.config.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import roomit.web1_2_bumblebee_be.domain.business.service.CustomBusinessDetailsService;
import roomit.web1_2_bumblebee_be.domain.member.service.CustomMemberDetailsService;
import roomit.web1_2_bumblebee_be.domain.token.config.JWTFilter;
import roomit.web1_2_bumblebee_be.domain.token.config.JWTUtil;
import roomit.web1_2_bumblebee_be.domain.token.config.LoginFilter;
import roomit.web1_2_bumblebee_be.domain.token.config.LogoutFilter;
import roomit.web1_2_bumblebee_be.domain.token.repository.RefreshRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomMemberDetailsService memberDetailsService;
    private final CustomBusinessDetailsService businessDetailsService;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(memberAuthenticationProvider())
                .authenticationProvider(businessAuthenticationProvider())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_BUSINESS
                ROLE_BUSINESS > ROLE_USER
                """);
    }

    @Bean
    public DaoAuthenticationProvider memberAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider businessAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(businessDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable); //csrf 비활성화
        //Cors 설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));
        http
                .formLogin(AbstractHttpConfigurer::disable); //form 로그인 비활성화
        http
                .logout(AbstractHttpConfigurer::disable); //form 로그아웃 비활성화
        http
                // 경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login/**","/").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/api/v1/member/signup").permitAll()
                        .requestMatchers("/api/v1/business/signup").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("/business").hasRole("BUSINESS")
                        .anyRequest().permitAll()); // permitAll()로 할시 모두 허용


        http
                .addFilterBefore(new JWTFilter(jwtUtil,bCryptPasswordEncoder()), LoginFilter.class);

        http
                //커스텀 로그인 필터 추가
                .addFilterAt(new LoginFilter(authenticationManager(http), jwtUtil,refreshRepository), UsernamePasswordAuthenticationFilter.class);
        http
                //커스텀 로그아웃 필터
                .addFilterBefore(new LogoutFilter(jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);
        http
                // 세션 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));//무상태



        return http.build();
    }
}
