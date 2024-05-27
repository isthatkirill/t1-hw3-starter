package isthatkirill.hwthree.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Kirill Emelyanov
 */

@Aspect
public class LoggingAspect {

    private final LoggerProperties loggerProperties;

    public LoggingAspect(LoggerProperties loggerProperties) {
        this.loggerProperties = loggerProperties;
    }

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println(loggerProperties.getTest());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        System.out.println("Incoming Request: " + request.getMethod() + " " + request.getRequestURI());
    }

    @AfterReturning("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logAfterReturning(JoinPoint joinPoint) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (response != null) {
            System.out.println("Outgoing Response: " + response.getStatus());
        }
    }
}
