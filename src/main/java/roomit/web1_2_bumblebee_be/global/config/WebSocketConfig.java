package roomit.web1_2_bumblebee_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/subscribe");
        config.setApplicationDestinationPrefixes("/publish");
    }

    @Override
    public void registerStompEndPoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-connect")
                .setAllowedOrigins("*");
    }
}
