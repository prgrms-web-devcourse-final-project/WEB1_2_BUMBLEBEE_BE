package roomit.main.global.config.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import roomit.main.global.token.config.JWTUtil;

import java.util.Map;

@Component
public class WebSocketAuthInterceptor extends HttpSessionHandshakeInterceptor {

    private final JWTUtil jwtUtil;

    public WebSocketAuthInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거
            try {
                // JWT 검증
                Claims claims = (Claims) jwtUtil.validateToken(token);
                attributes.put("username", claims.get("username", String.class)); // 사용자 이름 저장
                attributes.put("role", claims.get("role", String.class)); // 역할 저장
            } catch (Exception e) {
                return false; // 검증 실패 시 연결 거부
            }
        } else {
            return false; // Authorization 헤더 없으면 연결 거부
        }
        return true; // 검증 성공 시 연결 허용
    }
}
