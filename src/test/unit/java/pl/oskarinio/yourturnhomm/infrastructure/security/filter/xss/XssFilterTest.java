package pl.oskarinio.yourturnhomm.infrastructure.security.filter.xss;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class XssFilterTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @Captor
    ArgumentCaptor<HttpServletRequest> captor;

    private  XssFilter xssFilter;

    @BeforeEach
    void setUp(){
        xssFilter = new XssFilter();

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response = webUtilities.getResponse();
        filterChain = webUtilities.getFilterChain();
    }

    @Test
    @DisplayName("Opakowuje request używając XssRequestWrappera")
    void doFilterInternal_defaultValues() throws ServletException, IOException {
        xssFilter.doFilterInternal(request, response,filterChain);

        verify(filterChain).doFilter(captor.capture(), any());
        HttpServletRequest testRequest = captor.getValue();

        assertThat(testRequest).isInstanceOf(XssRequestWrapper.class);
    }
}
