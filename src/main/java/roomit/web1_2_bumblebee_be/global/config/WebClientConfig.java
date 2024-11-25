package roomit.web1_2_bumblebee_be.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://dapi.kakao.com") // 기본 API URL
                .defaultHeader("Authorization", "KakaoKey") // 인증 헤더 추가
                .build();
    }
}
