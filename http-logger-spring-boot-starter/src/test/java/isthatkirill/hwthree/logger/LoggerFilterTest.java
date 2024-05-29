package isthatkirill.hwthree.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Emelyanov
 */

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
class LoggerFilterTest {

    @Mock
    private LoggerProperties loggerProperties;

    @Spy
    @InjectMocks
    private LoggerFilter loggerFilter;

    @Test
    @SneakyThrows
    void testLoggingEnabledForGetMethod(CapturedOutput output) {
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String method = "GET";
        String uri = "/uri";
        int status = 200;

        request.setMethod(method);
        request.setRequestURI(uri);
        response.setStatus(status);

        when(loggerProperties.getMethods()).thenReturn(List.of("GET"));

        loggerFilter.doFilterInternal(request, response, filterChain);

        // check that output contains request details
        assertThat(output.getOut()).contains(method, uri, "LoggerFilter");
        // check that output contains response details
        assertThat(output.getOut()).contains(String.valueOf(status));

        verify(loggerProperties, times(1)).getMethods();
        verify(loggerFilter, times(1)).doFilterInternal(request, response, filterChain);
    }

    @Test
    @SneakyThrows
    void testLoggingDisabledForGetMethod(CapturedOutput output) {
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String method = "GET";
        String uri = "/uri";
        int status = 200;

        request.setMethod(method);
        request.setRequestURI(uri);
        response.setStatus(status);

        when(loggerProperties.getMethods()).thenReturn(List.of("POST"));

        loggerFilter.doFilterInternal(request, response, filterChain);

        // check that output is empty because logging for GET methods is disabled
        assertThat(output.getOut()).isEmpty();

        verify(loggerProperties, times(1)).getMethods();
        verify(loggerFilter, times(1)).doFilterInternal(request, response, filterChain);
    }

    @Test
    @SneakyThrows
    void testLoggingException(CapturedOutput output) {
        FilterChain filterChain = mock(FilterChain.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        String method = "GET";
        String uri = "/uri";
        String errorMessage = "Some error message";
        int status = 200;

        request.setMethod(method);
        request.setRequestURI(uri);
        response.setStatus(status);

        doThrow(new ServletException(new RuntimeException(errorMessage))).when(filterChain).doFilter(any(), any());

        loggerFilter.doFilterInternal(request, response, filterChain);

        // check that output contains request details
        assertThat(output.getOut()).contains(method, uri, "LoggerFilter");
        // check that output contains information about failed request
        assertThat(output.getOut()).contains("FAILED", errorMessage);

        verifyNoInteractions(loggerProperties);
        verify(loggerFilter, times(1)).doFilterInternal(request, response, filterChain);
    }

}