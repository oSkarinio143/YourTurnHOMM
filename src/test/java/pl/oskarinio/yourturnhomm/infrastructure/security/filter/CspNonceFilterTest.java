package pl.oskarinio.yourturnhomm.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

class CspNonceFilterTest {
    private static final int ATTRIBUTE_SIZE = 32;
    private static final String ATTRIBUTE_NAME = "cspNonce";

    private CspNonceFilter cspNonceFilter = new CspNonceFilter();

    @Test
    @DisplayName("Ustawia poprawny nonce i dodaje do request")
    void testDoFilter_correctValues() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        cspNonceFilter.doFilter(request, response, filterChain);
        String randomEncodedBytes = request.getAttribute(ATTRIBUTE_NAME).toString();
        byte[] nonceArray = Base64.getDecoder().decode(randomEncodedBytes);
        assertThat(nonceArray).hasSize(ATTRIBUTE_SIZE);
    }

    @Test
    @DisplayName("Nadpisuje istniejący nonce nowym i dodaje do request")
    void testDoFilter_cspNonceIsSet_newTrueNonceIsSet() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(ATTRIBUTE_NAME, "fakeNonce");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        cspNonceFilter.doFilter(request, response, filterChain);
        String randomEncodedBytes = request.getAttribute(ATTRIBUTE_NAME).toString();
        byte[] nonceArray = Base64.getDecoder().decode(randomEncodedBytes);
        assertThat(nonceArray).hasSize(ATTRIBUTE_SIZE);
    }
}
