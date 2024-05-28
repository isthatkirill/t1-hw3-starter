package isthatkirill.hwthree.autoconfigure;

import isthatkirill.hwthree.logger.LoggerProperties;
import isthatkirill.hwthree.logger.LoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnMissingBean(LoggingFilter.class)
    public LoggingFilter loggingFilter(LoggerProperties loggerProperties) {
        return new LoggingFilter(loggerProperties);
    }

}
