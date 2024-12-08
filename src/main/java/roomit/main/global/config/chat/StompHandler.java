package roomit.main.global.config.chat;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
//import roomit.main.global.token.config.JWTUtil;
//import roomit.main.global.util.BumblebeeStringUtil;
//
//import java.util.Set;
//
@Component
public class StompHandler implements ChannelInterceptor {
//
//    private static final String REDIS_MESSAGE_KEY_FORMAT = "chat:room:{}:messages";
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final JWTUtil jwtUtil; // JWT 파싱 유틸리티
//
//    public StompHandler(RedisTemplate<String, Object> redisTemplate, JWTUtil jwtUtil) {
//        this.redisTemplate = redisTemplate;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        StompCommand command = accessor.getCommand();
//
//        if (StompCommand.SUBSCRIBE.equals(command)) {
//            handleSubscribe(accessor);
//        }
//
//        return message;
//    }
//
//    private void handleSubscribe(StompHeaderAccessor accessor) {
//        String destination = accessor.getDestination();
//
//        if (destination == null || !destination.startsWith("/sub/chat/")) {
//            log.warn("Invalid subscription destination: {}", destination);
//            return;
//        }
//
//        String roomId = destination.replace("/sub/chat/", "");
//        if (roomId.isEmpty()) {
//            log.warn("Missing roomId in subscription destination");
//            return;
//        }
//
//        // JWT 토큰에서 사용자 ID 추출
//        String token = extractToken(accessor);
//        if (token == null) {
//            log.warn("Missing Authorization header or token");
//            return;
//        }
//
//        String currentUserId;
//        try {
//            currentUserId = jwtUtil.getUsername(token);
//        } catch (Exception e) {
//            log.warn("Invalid JWT token: {}", e.getMessage());
//            return;
//        }
//
//        // 메시지 읽음 처리 (Redis 사용)
//        markMessagesAsRead(Long.valueOf(roomId), currentUserId);
//    }
//
//    private void markMessagesAsRead(Long roomId, String currentUserId) {
//        String redisKey = BumblebeeStringUtil.format(REDIS_MESSAGE_KEY_FORMAT, roomId); // 메시지 저장 키
//        String unreadKey = redisKey + ":unread"; // 읽음 상태 저장 키
//
//        // Redis에서 읽지 않은 발신자 목록 조회
//        Set<Object> unreadSenders = redisTemplate.opsForHash().keys(unreadKey);
//        if (unreadSenders == null || unreadSenders.isEmpty()) {
//            log.info("No unread messages in room [{}]", roomId);
//            return;
//        }
//
//        // 메시지 목록 순회 및 읽음 처리
//        unreadSenders.forEach(sender -> {
//            if (!sender.equals(currentUserId)) { // 본인의 메시지는 읽음 처리 제외
//                // 메시지 읽음 처리 (ZSet 데이터와 매칭 가능)
//                redisTemplate.opsForHash().delete(unreadKey, sender);
//                log.debug("Marked messages as read for room [{}] from sender [{}] by user [{}]", roomId, sender, currentUserId);
//            } else {
//                log.debug("Skipping marking messages as read for sender [{}] in room [{}], as it matches current user [{}]", sender, roomId, currentUserId);
//            }
//        });
//
//        // 추가로 메시지 데이터 검증 또는 삭제가 필요한 경우
//        redisTemplate.opsForZSet().removeRange(redisKey, 0, -1); // 필요에 따라 처리
//    }
//
//    private String extractToken(StompHeaderAccessor accessor) {
//        String authHeader = accessor.getFirstNativeHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7); // "Bearer " 제거
//        }
//        return null;
//    }
//
}
