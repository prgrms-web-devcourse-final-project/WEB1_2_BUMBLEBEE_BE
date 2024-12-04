package roomit.main.global.token.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import roomit.main.domain.business.entity.Business;
import roomit.main.domain.business.service.CustomBusinessDetailsService;
import roomit.main.domain.member.entity.Member;
import roomit.main.domain.member.entity.Role;
import roomit.main.domain.member.entity.Sex;
import roomit.main.domain.member.service.CustomMemberDetailsService;
import roomit.main.global.oauth2.dto.PROVIDER;

@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomMemberDetailsService customMemberDetailsService;
    private final CustomBusinessDetailsService customBusinessDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String headerAuth = request.getHeader("Authorization");

        // 헤더 값이 없거나, 토큰 값이 "Bearer "로 시작하지 않으면 다음 필터로 넘김
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = headerAuth.substring(7);

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰 카테고리가 무엇인지
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            String email = claims.get("username").toString();
            String role = claims.get("role").toString();

            UserDetails principal;

            if ("ROLE_USER".equals(role)) {
                Member member = Member.builder()
                        .memberEmail(email)
                        .birthDay(LocalDate.now())
                        .memberNickName("멤버테스트")
                        .memberPwd("Dummy1423!")
                        .memberSex(Sex.MALE)
                        .memberRole(Role.ROLE_USER)
                        .memberPhoneNumber("010-1111-2222")
                        .provider(PROVIDER.BASIC)
                        .passwordEncoder(passwordEncoder)
                        .build();
                principal = customMemberDetailsService.loadUserByUsername(member.getMemberEmail());
            } else if ("ROLE_BUSINESS".equals(role)) { //모든 정보가 들어가는 문제가 있음
                Business business = Business.builder()
                        .businessEmail(email)
                        .businessName("더미")
                        .businessPwd("Dummy1423!")
                        .passwordEncoder(passwordEncoder)
                        .businessNum("321-12-74312")
                        .build();
                principal = customBusinessDetailsService.loadUserByUsername(business.getBusinessEmail());
            } else {
                throw new RuntimeException("잘못된 역할 정보: " + role); // 어드민 역할과 사용자 정의 예외 추가
            }

            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    Arrays.asList(new SimpleGrantedAuthority(role))
            );

            // SecurityContext에 인증/인가 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("Security Context Authentication: {}", authToken);

        } catch (Exception e) {
                handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 예외적으로 필터링하지 않을 경로들
        List<String> excludedPaths = List.of(
                "/",
                "/index.html",
                "/static/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/login/**",
                "/toss/**",
                "/api/v1/member/signup",
                "/api/v1/business/signup",
                "/reissue",
                "/api/v1/studyroom/workplace/**",
                "/api/v1/studyroom/search",
                "/api/v1/workplace/info/**",
                "/api/v1/review/workplace/**",
                "/api/v1/workplace/distance",
                "/api/v1/workplace/distance",
                "/ws/**",
                //결제
                "/api/v1/payments/toss/success",
                "/api/v1/payments/toss/fail",
                "/api/v1/recommend/**"
        );

        AntPathMatcher pathMatcher = new AntPathMatcher();
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
    }

    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
    }
}
