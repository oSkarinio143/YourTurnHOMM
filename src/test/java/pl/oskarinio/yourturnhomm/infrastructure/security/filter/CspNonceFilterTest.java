package pl.oskarinio.yourturnhomm.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class CspNonceFilterTest {
    private static final int ATTRIBUTE_SIZE = 32;
    private static final String ATTRIBUTE_NAME = "cspNonce";
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    private CspNonceFilter cspNonceFilter;

    @BeforeEach
    void setUp(){
        cspNonceFilter = new CspNonceFilter();

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response =  webUtilities.getResponse();
        filterChain = webUtilities.getFilterChain();
    }

    @Test
    @DisplayName("Ustawia poprawny nonce i dodaje do request")
    void testDoFilter_correctValues() throws ServletException, IOException {
        cspNonceFilter.doFilter(request, response, filterChain);

        String randomEncodedBytes = request.getAttribute(ATTRIBUTE_NAME).toString();
        byte[] nonceArray = Base64.getDecoder().decode(randomEncodedBytes);
        assertThat(nonceArray).hasSize(ATTRIBUTE_SIZE);
    }

    @Test
    @DisplayName("Nadpisuje istniejący nonce nowym i dodaje do request")
    void testDoFilter_cspNonceIsSet_newTrueNonceIsSet() throws ServletException, IOException {
        request.setAttribute(ATTRIBUTE_NAME, "fakeNonce");

        cspNonceFilter.doFilter(request, response, filterChain);
        String randomEncodedBytes = request.getAttribute(ATTRIBUTE_NAME).toString();
        byte[] nonceArray = Base64.getDecoder().decode(randomEncodedBytes);
        assertThat(nonceArray).hasSize(ATTRIBUTE_SIZE);
    }
}
