package roomit.main.global.config.security;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpMessageDestMatchers("/pub").authenticated() // **WebSocket 메시지 전송 인증 추가**
                .simpSubscribeDestMatchers("/sub/chat/**").permitAll(); // 구독 요청 인증
//                .anyMessage().denyAll(); // 다른 요청은 모두 거부
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true; // **Cross-Origin WebSocket 요청 허용**
    }
}
