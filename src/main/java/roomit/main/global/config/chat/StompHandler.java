package roomit.main.global.config.chat;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
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


}
