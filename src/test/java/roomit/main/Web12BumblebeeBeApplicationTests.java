package roomit.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "application-test.properties")
class Web12BumblebeeBeApplicationTests {

    @Test
    void contextLoads() {
    }

}
