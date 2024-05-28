package isthatkirill.hwthree.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            if (!loggerProperties.getMethods().contains(requestWrapper.getMethod())) return;
            logRequest(requestWrapper);
            logResponse(responseWrapper, startTime);
        } catch (Throwable e) {
            logRequest(requestWrapper);
            handleException(e.getCause(), responseWrapper);
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        String requestBody = getBody(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        String requestHeaders = getRequestHeadersAsString(requestWrapper);
        log.info("Request: method = [{}], uri = [{}], request body = [{}], request headers = [{}]",
                requestWrapper.getMethod(), requestWrapper.getRequestURI(), requestBody, requestHeaders);
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper, long startTime) {
        long timeTaken = System.currentTimeMillis() - startTime;
        String responseBody = getResponseHeadersAsString(responseWrapper);
        String responseHeaders = getResponseHeadersAsString(responseWrapper);
        log.info("Response: response status = [{}], response body = [{}], time taken = [{} ms], response headers = [{}]",
                responseWrapper.getStatus(), responseBody, timeTaken, responseHeaders);
    }

    private void handleException(Throwable cause, ContentCachingResponseWrapper responseWrapper) throws IOException {
        String className = cause.getClass().getSimpleName();
        String message = cause.getMessage();
        responseWrapper.resetBuffer();
        responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseWrapper.getWriter().write("Error occurred: " + className + " [" + message + "]");
        responseWrapper.getWriter().flush();
        log.error("[FAILED]: error = [{}], response status = [{}],  response body = [{}]",
                className, responseWrapper.getStatus(), message);
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

    private String getBody(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}