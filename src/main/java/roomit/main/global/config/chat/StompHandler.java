package roomit.main.global.config.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    public StompHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            connectToChatRoom(accessor);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            disconnectFromChatRoom(accessor);
        }else if (StompCommand.SUBSCRIBE.equals(command)) {
            handleSubscribe(accessor);
        }
        return message;
    }

    private void connectToChatRoom(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader("roomId");
        String userId = accessor.getFirstNativeHeader("userId");

        redisTemplate.opsForSet().add("chatRoom:" + chatRoomId, userId);

        updateUnreadMessages(chatRoomId, userId);
    }

    private void disconnectFromChatRoom(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader("roomId");
        String userId = accessor.getFirstNativeHeader("userId");

        redisTemplate.opsForSet().remove("chatRoom:" + chatRoomId, userId);
    }

    private void updateUnreadMessages(String chatRoomId, String userId) {

    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination(); // 구독 경로 확인
        String username = accessor.getFirstNativeHeader("username");
        log.info("User [{}] subscribed to room [{}]", username, destination);

        if (!destination.startsWith("/sub/chat/")) {
            log.warn("Invalid subscription destination: {}", destination);
            throw new IllegalArgumentException("Invalid subscription destination");
        }

        String roomId = destination.replace("/sub/chat/", ""); // roomId 추출
        log.info("User [{}] subscribed to room [{}]", username, roomId);
    }

}
