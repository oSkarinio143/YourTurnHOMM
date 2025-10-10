package pl.oskarinio.yourturnhomm.infrastructure.security.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

//Filter generuje i udostępnia nonce, zapobiega niechcianym skryptom
public class CspNonceFilter extends GenericFilterBean {
    private static final int NONCE_SIZE = 32;
    private static final String CSP_NONCE_ATTRIBUTE = "cspNonce";

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        byte[] nonceArray = new byte[NONCE_SIZE];

        secureRandom.nextBytes(nonceArray);

        String nonce = Base64.getEncoder().encodeToString(nonceArray);
        request.setAttribute(CSP_NONCE_ATTRIBUTE, nonce);

        chain.doFilter(request, new CSPNonceResponseWrapper(response, nonce));
    }

    private static class CSPNonceResponseWrapper extends HttpServletResponseWrapper {
        private final String nonce;

        CSPNonceResponseWrapper(HttpServletResponse response, String nonce) {
            super(response);
            this.nonce = nonce;
        }

        @Override
        public void setHeader(String name, String value) {
            if (name.equals("Content-Security-Policy") && StringUtils.isNotBlank(value)) {
                super.setHeader(name, value.replace("{nonce}", nonce));
            } else {
                super.setHeader(name, value);
            }
        }

        @Override
        public void addHeader(String name, String value) {
            if (name.equals("Content-Security-Policy") && StringUtils.isNotBlank(value)) {
                super.addHeader(name, value.replace("{nonce}", nonce));
            } else {
                super.addHeader(name, value);
            }
        }
    }
}