package isthatkirill.hwthree.sample;

import isthatkirill.hwthree.logger.LoggingErrorAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author Kirill Emelyanov
 */

@SpringBootTest
class LoggerSampleApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext.getBean(LoggingErrorAspect.class)).isNotNull();
    }

}