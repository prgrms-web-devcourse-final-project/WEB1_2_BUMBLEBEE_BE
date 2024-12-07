package roomit.main.global.config.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import roomit.main.domain.business.service.CustomBusinessDetailsService;
import roomit.main.domain.member.service.CustomMemberDetailsService;
import roomit.main.global.oauth2.config.CustomSuccessHandler;
import roomit.main.global.oauth2.service.CustomOAuth2UserService;
import roomit.main.global.token.config.JWTFilter;
import roomit.main.global.token.config.JWTUtil;
import roomit.main.global.token.config.LoginFilter;
import roomit.main.global.token.config.LogoutFilter;
import roomit.main.global.token.repository.RefreshRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Value("${cors.url}")
    private String corsUrl;

    @Value("${cors.front.url}")
    private String frontUrl;

    @Value("${cors.ai.url}")
    private String aiUrl;

    private final JWTUtil jwtUtil;
    private final CustomMemberDetailsService memberDetailsService;
    private final CustomBusinessDetailsService businessDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider memberProvider = new DaoAuthenticationProvider();
        memberProvider.setUserDetailsService(memberDetailsService);
        memberProvider.setPasswordEncoder(bCryptPasswordEncoder());

        DaoAuthenticationProvider businessProvider = new DaoAuthenticationProvider();
        businessProvider.setUserDetailsService(businessDetailsService);
        businessProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return new CustomProviderManager(Arrays.asList(memberProvider, businessProvider));
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable); //csrf 비활성화
        //Cors 설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(
                            List.of(frontUrl,corsUrl,aiUrl));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie", "refresh"));
                        return configuration;
                    }
                })));
        http
                .formLogin(AbstractHttpConfigurer::disable); //form 로그인 비활성화
        http
                .logout(AbstractHttpConfigurer::disable); //form 로그아웃 비활성화
                //HTTP Basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        //oauth2
        http
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .successHandler(customSuccessHandler)
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Oauth2 인증 실패"); // 로그인 페이지로 리다이렉트하지 않음
                })
            );

        http
                // 경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        //정적 리소스 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/index.html").permitAll()
                        //모두 허용
                        .requestMatchers("/login/**","/", "/health").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/noauth").permitAll()
                        .requestMatchers("/api/v1/member/signup").permitAll()
                        .requestMatchers("/api/v1/business/signup").permitAll()
                        .requestMatchers("/toss/**").permitAll()
                        .requestMatchers("/api/subscribe/**").permitAll()

                        .requestMatchers("/api/v1/recommend/**").hasRole("USER")

                        //PresignedURL
                        .requestMatchers(HttpMethod.GET,"/api/generate-presigned-url").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.DELETE,"/api/delete-folder").hasRole("BUSINESS")

                        //멤버 권한 설정
                        .requestMatchers("/api/v1/member").hasRole("USER")

                        //비즈니스 권한 설정
                        .requestMatchers("/api/v1/business").hasRole("BUSINESS")

                        //스터디룸 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/workplace/**").permitAll()           //사업장의 스터디룸 찾기
                        .requestMatchers(HttpMethod.POST, "/api/v1/studyroom/available").permitAll()             //예약가능한 스터디룸 찾기
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/search/**").permitAll()              //스터디룸의 예약 가능한 시간대
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/**").permitAll()                     //스터디룸 상세 정보
                        .requestMatchers(HttpMethod.POST,"/api/v1/studyroom").hasRole("BUSINESS")               //스터디룸 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/studyroom").hasRole("BUSINESS")                //스터디룸 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/studyroom/**").hasRole("BUSINESS")          //스터디룸 삭제

                        // 알림
                        .requestMatchers(HttpMethod.GET, "/api/v1/sub/list/**").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.GET, "/api/v1/subReservation/list/**").hasRole("BUSINESS")

                        //사업장 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace/info/**").permitAll() //사업장 정보 조회
//                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace").permitAll() //사업장 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace/business").hasRole("BUSINESS") //접속한 사업자 사업자ID로 사업장 조회**
                        .requestMatchers(HttpMethod.POST,"/api/v1/workplace/distance").permitAll() //위치 기반 주변 사업장
                        .requestMatchers(HttpMethod.POST,"/api/v1/workplace").hasRole("BUSINESS") //사업장 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/workplace/**").hasRole("BUSINESS") //사업장 정보 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/workplace/**").hasRole("BUSINESS") //사업장 삭제

                        //예약 권한 설정
                        .requestMatchers(HttpMethod.POST,"/api/v1/reservations/**").hasAnyRole("USER") //예약 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/reservations/**").hasAnyRole("USER","BUSINESS") //예약 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/reservations/**").hasAnyRole("BUSINESS","USER") //예약 삭제
                        .requestMatchers(HttpMethod.GET,"/api/v1/all/reservations/member/**").hasAnyRole("USER") //특정 멤버의 최근 예약 단건 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/reservations/**").hasAnyRole("USER") //특정 멤버의 최근 예약 전체 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/reservations/workplace/**").hasAnyRole("BUSINESS","USER") //특정 사업장의 예약 찾기

                        //결제 권한 설정
                        .requestMatchers(HttpMethod.POST,"/api/v1/payments/toss/**").hasRole("USER") //결제 검증 및 서버 저장
                        .requestMatchers(HttpMethod.GET,"/api/v1/payments/toss/**").hasRole("USER")

                        //알림 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/notification/member").hasRole("USER") //회원 알림 내역 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/notification/business").hasRole("BUSINESS") //사업자 알림 내역 조회

                        //리뷰 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/workplace/**").permitAll() //후기 전체 조회<페이징 >
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/me").hasAnyRole("BUSINESS","USER") //본인이 작성한 후기 조회
                        .requestMatchers(HttpMethod.POST,"/api/v1/review/register").hasAnyRole("BUSINESS","USER") //후기 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/review/update/**").hasAnyRole("BUSINESS","USER") //후기 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/review/**").hasAnyRole("BUSINESS","USER") //후기 삭제

                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/chat/room/**").hasAnyRole("BUSINESS","USER")

                        .requestMatchers("/api/v1/chat/create").hasAnyRole("BUSINESS","USER")
                        .requestMatchers("/api/v1/chat/room").hasAnyRole("BUSINESS","USER")
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil,bCryptPasswordEncoder(),memberDetailsService,businessDetailsService), LoginFilter.class);

        http
                //커스텀 로그인 필터 추가
                .addFilterAt(new LoginFilter(authenticationManager(), jwtUtil,refreshRepository), UsernamePasswordAuthenticationFilter.class);
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
