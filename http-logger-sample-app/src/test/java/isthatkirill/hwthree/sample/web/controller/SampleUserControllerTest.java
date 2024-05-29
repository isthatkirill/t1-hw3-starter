package isthatkirill.hwthree.sample.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.hwthree.logger.LoggerFilter;
import isthatkirill.hwthree.logger.LoggerProperties;
import isthatkirill.hwthree.sample.web.dto.SampleUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kirill Emelyanov
 */

@ActiveProfiles("logging-on")
@ExtendWith(OutputCaptureExtension.class)
@WebMvcTest(controllers = SampleUserController.class)
class SampleUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private LoggerFilter loggerFilter;

    @SpyBean
    private LoggerProperties loggerProperties;

    @Test
    @SneakyThrows
    void loggerFilterIsWorkingTest(CapturedOutput output) {
        SampleUser sampleUser = new SampleUser(18, "ivan", "ivanov");
        String uri = "/sample/user";

        mvc.perform(post(uri)
                        .content(objectMapper.writeValueAsString(sampleUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.age").value(sampleUser.getAge()))
                .andExpect(jsonPath("$.name").value(sampleUser.getName()))
                .andExpect(jsonPath("$.surname").value(sampleUser.getSurname()));

        // check that loggerFilter.doFilter(...) invoked
        verify(loggerFilter, times(1))
                .doFilter(any(), any(), any());

        // check that output contains request details
        assertThat(output.getOut()).contains(sampleUser.getName(), uri, "POST", "LoggerFilter");
        // check that output contains response details
        assertThat(output.getOut()).contains("Content-Type: application/json");
    }

    @Test
    @SneakyThrows
    void loggerFilterIsWorkingWithExceptionTest(CapturedOutput output) {
        String uri = "/sample/user/exception";

        mvc.perform(get(uri)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isInternalServerError());

        // check that loggerFilter.doFilter(...) invoked
        verify(loggerFilter, times(1))
                .doFilter(any(), any(), any());

        // check that output contains request details
        assertThat(output.getOut()).contains(uri, "GET", "LoggerFilter");
        // check that output contains information about failed request
        assertThat(output.getOut()).contains("FAILED");
    }

}



