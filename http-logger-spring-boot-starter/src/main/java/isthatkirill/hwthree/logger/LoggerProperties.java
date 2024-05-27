package isthatkirill.hwthree.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kirill Emelyanov
 */

@Component
@ConfigurationProperties(prefix = "http.logging")
public class LoggerProperties {

    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

}

