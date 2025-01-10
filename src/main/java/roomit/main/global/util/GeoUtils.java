package roomit.main.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class GeoUtils {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GeoUtils(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Map<String, Double> geocoding(String address) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", address)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 동기식 호출

            JsonNode document = objectMapper.readTree(response).path("documents").get(0);

            Double latitude = Double.valueOf(document.path("y").asText());
            Double longitude = Double.valueOf(document.path("x").asText());

            return Map.of(
                    "latitude", latitude,
                    "longitude", longitude
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON response", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract coordinates from response", e);
        }
    }

    public Map<String, Double> parseAddress(String workplaceAddress) {
        try {
            String[] parts = workplaceAddress.split(",", 2);
            String roadAddress = parts[0].trim();
            return geocoding(roadAddress);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid workplace address", e);
        }
    }
}
