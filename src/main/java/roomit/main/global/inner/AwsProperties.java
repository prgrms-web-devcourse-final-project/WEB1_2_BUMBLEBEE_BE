package roomit.main.global.inner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "amazon.aws")
public class AwsProperties {

  // getters and setters
  private String bucket;
  private String region;

}