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

import java.io.*;
import java.util.Arrays;
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
        String params = requestWrapper.getParameterMap().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + Arrays.toString(entry.getValue()))
                                .collect(Collectors.joining(", "));
        log.info("Request: method = [{}], uri = [{}], body = [{}], params = [{}], headers = [{}]",
                requestWrapper.getMethod(), requestWrapper.getRequestURI(), requestBody, params, requestHeaders);
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper, long startTime) {
        long timeTaken = System.currentTimeMillis() - startTime;
        String responseBody = getBody(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        String responseHeaders = getResponseHeadersAsString(responseWrapper);
        log.info("Response: status = [{}], body = [{}], time taken = [{} ms], headers = [{}]",
                responseWrapper.getStatus(), responseBody, timeTaken, responseHeaders);
    }

    private void handleException(Throwable cause, ContentCachingResponseWrapper responseWrapper) throws IOException {
        String className = cause.getClass().getSimpleName();
        String message = cause.getMessage();
        responseWrapper.resetBuffer();
        responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseWrapper.getWriter().write("Error occurred: " + className + " [" + message + "]");
        responseWrapper.getWriter().flush();
        log.error("[FAILED] Response: error = [{}], status = [{}],  body = [{}]",
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
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(contentAsByteArray);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, characterEncoding);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            StringBuilder bodyBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                bodyBuilder.append(line.trim());
            }
            return bodyBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}