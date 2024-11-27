package roomit.main.global.config.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import roomit.main.domain.business.service.CustomBusinessDetailsService;
import roomit.main.domain.member.service.CustomMemberDetailsService;
import roomit.main.domain.oauth2.config.CustomSuccessHandler;
import roomit.main.domain.oauth2.service.CustomOAuth2UserService;
import roomit.main.domain.token.config.JWTFilter;
import roomit.main.domain.token.config.JWTUtil;
import roomit.main.domain.token.config.LoginFilter;
import roomit.main.domain.token.config.LogoutFilter;
import roomit.main.domain.token.repository.RefreshRepository;


import java.util.Arrays;
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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

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
                //HTTP Basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        //oauth2
        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/noauth") // 로그인 페이지 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)) // 사용자 정보 로드
                        .successHandler(customSuccessHandler) // 성공 핸들러
                );
        http
                // 경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        //모두 허용
                        .requestMatchers("/login/**","/").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/noauth").permitAll()
                        .requestMatchers("/api/v1/member/signup").permitAll()
                        .requestMatchers("/api/v1/business/signup").permitAll()
                        .requestMatchers("/toss/**").permitAll()


                        //멤버 권한 설정
                        .requestMatchers("/api/v1/member").hasRole("USER")

                        //비즈니스 권한 설정
                        .requestMatchers("/api/v1/business").hasRole("BUSINESS")

                        //스터디룸 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/workplace/**").permitAll() //사업장의 스터디룸 찾기
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/search").permitAll() //예약가능한 스터디룸 찾기
                        .requestMatchers(HttpMethod.GET,"/api/v1/studyroom/**").hasRole("USER") //최근 예약한 스터디룸 보여주기
                        .requestMatchers(HttpMethod.POST,"/api/v1/studyroom").hasRole("BUSINESS") //스터디룸 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/studyroom").hasRole("BUSINESS") //스터디룸 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/studyroom/**").hasRole("BUSINESS") //스터디룸 삭제

                        //사업장 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace/info/**").permitAll() //사업장 정보 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace").permitAll() //사업장 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace/business").permitAll() //사업자 사업장 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/workplace/distance").permitAll() //위치 기반 주변 사업장
                        .requestMatchers(HttpMethod.POST,"/api/v1/workplace").hasRole("BUSINESS") //사업장 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/workplace/**").hasRole("BUSINESS") //사업장 정보 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/workplace/**").hasRole("BUSINESS") //사업장 삭제

                        //예약 권한 설정
                        .requestMatchers(HttpMethod.POST,"/api/v1/reservation").hasAnyRole("BUSINESS","USER") //예약 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/reservation/**").hasAnyRole("BUSINESS","USER") //예약 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/reservation/**").hasAnyRole("BUSINESS","USER") //예약 삭제
                        .requestMatchers(HttpMethod.GET,"/api/v1/all/reservations/member/**").hasAnyRole("BUSINESS","USER") //특정 멤버의 최근 예약 단건 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/reservation/**").hasAnyRole("BUSINESS","USER") //특정 멤버의 최근 예약 전체 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/reservation/workplace/**").hasAnyRole("BUSINESS","USER") //특정 사업장의 예약 찾기

                        //결제 권한 설정

                        //알림 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/notification/member").hasRole("USER") //회원 알림 내역 조회
                        .requestMatchers(HttpMethod.GET,"/api/v1/notification/business").hasRole("BUSINESS") //사업자 알림 내역 조회

                        //리뷰 권한 설정
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/workplace/**").permitAll() //후기 전체 조회<페이징 >
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/me").hasAnyRole("BUSINESS","USER") //본인이 작성한 후기 조회
                        .requestMatchers(HttpMethod.POST,"/api/v1/review/register").hasAnyRole("BUSINESS","USER") //후기 등록
                        .requestMatchers(HttpMethod.PUT,"/api/v1/review/update/**").hasAnyRole("BUSINESS","USER") //후기 수정
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/review/**").hasAnyRole("BUSINESS","USER") //후기 삭제

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
