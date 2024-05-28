package isthatkirill.hwthree.logger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "http.logging")
public class LoggerProperties {

    private boolean enabled = true;
    private List<String> methods = List.of("GET", "POST", "PUT", "PATCH", "DELETE",
            "OPTIONS", "HEAD");

}

