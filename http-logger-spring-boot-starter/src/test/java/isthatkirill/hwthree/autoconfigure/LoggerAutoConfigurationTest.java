package isthatkirill.hwthree.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest(classes = LoggerAutoConfiguration.class)
class LoggerAutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertTrue(context.containsBean("loggingFilter"));
    }


}