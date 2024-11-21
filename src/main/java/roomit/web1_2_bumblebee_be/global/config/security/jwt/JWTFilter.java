package roomit.web1_2_bumblebee_be.global.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import roomit.web1_2_bumblebee_be.domain.member.dto.CustomMemberDetails;
import roomit.web1_2_bumblebee_be.domain.member.entity.Member;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        log.info("token: " +authorization);
        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")){

            filterChain.doFilter(request,response);

            return;
        }
        //Bearer 부분 제거 후 토큰 값 획득
        String token = authorization.substring(7);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)){
            filterChain.doFilter(request,response);

            return;
        }

        //토큰에서 값 획득
        String email = jwtUtil.getUsername(token);
        Role role = jwtUtil.getRole(token);

        log.info("email : " + email);
        log.info("role : " + role);


        //Member 엔티티 생성하여 값 set
        Member member = Member.builder()
                .memberEmail(email)
                .memberPwd("dummy")
                .memberRole(role)
                .build();

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null , customMemberDetails.getAuthorities());

        // Spring Security Context에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
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
}
