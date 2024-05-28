package isthatkirill.hwthree.logger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kirill Emelyanov
 */

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "http.logging")
public class LoggerProperties {

    private boolean enabled;

}

