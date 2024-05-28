package isthatkirill.hwthree.autoconfigure;

import isthatkirill.hwthree.logger.LoggerFilter;
import isthatkirill.hwthree.logger.LoggerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kirill Emelyanov
 */

@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "http.logging.enabled", havingValue = "true", matchIfMissing = true)
    public LoggerFilter loggingFilter(LoggerProperties loggerProperties) {
        return new LoggerFilter(loggerProperties);
    }

}
