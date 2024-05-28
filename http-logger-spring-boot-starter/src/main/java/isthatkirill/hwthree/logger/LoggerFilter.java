package isthatkirill.hwthree.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@RequiredArgsConstructor
public class LoggerFilter extends OncePerRequestFilter {

    private final LoggerProperties loggerProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());

        log.info("Request received: method = [{}], uri = [{}], request body = [{}]",
                request.getMethod(), request.getRequestURI(), requestBody);
        log.info("Request headers = [{}]", getRequestHeadersAsString(request));

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (ServletException e) {
            if (loggerProperties.getLogErrors().isEnabled()) {
                logException(e.getRootCause(), responseWrapper);
            }
            return;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
        log.info("Request processing finished:  response status = [{}], response body = [{}], time taken = [{}]",
                response.getStatus(), responseBody, timeTaken);
        log.info("Response headers = [{}]", getResponseHeadersAsString(response));
        responseWrapper.copyBodyToResponse();
    }

    private void logException(Throwable cause, ContentCachingResponseWrapper wrapper) throws IOException {
        String className = cause.getClass().getSimpleName();
        String message = cause.getMessage();
        wrapper.resetBuffer();
        wrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        wrapper.getWriter().write("Error occurred: " + className + " [" + message + "]");
        wrapper.getWriter().flush();
        log.error("Request processing failed: error = [{}], response body = [{}], response status = [{}], ",
                className, message, wrapper.getStatus());
        log.info("Response headers = [{}]", getResponseHeadersAsString(wrapper));
        wrapper.copyBodyToResponse();
    }

    private String getRequestHeadersAsString(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " +
                        String.join(", ", Collections.list(request.getHeaders(headerName))))
                .collect(Collectors.joining(" || "));
    }

    private String getResponseHeadersAsString(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(headerName -> headerName + ": " + String.join(", ", response.getHeaders(headerName)))
                .collect(Collectors.joining(" || "));
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}