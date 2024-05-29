package isthatkirill.hwthree.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
@ActiveProfiles("logging-off")
class LoggerSampleApplicationLoggingOffTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void loggingOffTest() {
        assertFalse(context.containsBean("loggingFilter"));
    }

}
