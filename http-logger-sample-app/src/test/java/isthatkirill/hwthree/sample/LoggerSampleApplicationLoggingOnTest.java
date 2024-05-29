package isthatkirill.hwthree.sample;

import isthatkirill.hwthree.logger.LoggerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("logging-on")
class LoggerSampleApplicationLoggingOnTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private LoggerProperties loggerProperties;

    @Test
    void loggingOnTest() {
        assertTrue(context.containsBean("loggingFilter"));
    }

    @Test
    void checkLoggerPropertiesTest() {
        assertThat(loggerProperties.getMethods()).hasSize(3)
                .containsExactlyInAnyOrder("GET", "POST", "PUT");
    }

}
