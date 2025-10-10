package pl.oskarinio.yourturnhomm.infrastructure.security.filter.xss;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Filter wykorzystuje wrapper, czyści przychodzące dane, zapobiega atakom xss
@Component
@Order(1)
public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        XssRequestWrapper wrappedRequest = new XssRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
    }
}
