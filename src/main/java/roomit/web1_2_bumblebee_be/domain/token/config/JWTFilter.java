package roomit.web1_2_bumblebee_be.domain.token.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;
import roomit.web1_2_bumblebee_be.domain.business.dto.CustomBusinessDetails;
import roomit.web1_2_bumblebee_be.domain.business.entity.Business;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

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

            Object principal;

            if ("ROLE_USER".equals(role)) {
                Member member = Member.builder()
                        .memberEmail(email)
                        .memberPwd("dummy")
                        .memberRole(Role.ROLE_USER)
                        .build();
                principal = new CustomMemberDetails(member);
            } else if ("ROLE_BUSINESS".equals(role)) { //모든 정보가 들어가는 문제가 있음
                Business business = Business.builder()
                        .businessEmail(email)
                        .businessName("더미")
                        .businessPwd("Dummy1423!")
                        .passwordEncoder(passwordEncoder)
                        .businessNum("321-12-74312")
                        .build();
                principal = new CustomBusinessDetails(business);
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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().startsWith("/login")) {

            return true;
        }
        if (request.getRequestURI().startsWith("/api/v1/member/signup")) {

            return true;
        }
        if (request.getRequestURI().startsWith("/api/v1/business/signup")) {

            return true;
        }
        return false;
    }

    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
    }
}
