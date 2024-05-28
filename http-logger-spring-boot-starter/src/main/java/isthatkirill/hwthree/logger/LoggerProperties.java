package isthatkirill.hwthree.logger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "http.logging")
public class LoggerProperties {

    private boolean enabled = true;

}

