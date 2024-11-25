package roomit.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Web12BumblebeeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Web12BumblebeeBeApplication.class, args);

    }

}
