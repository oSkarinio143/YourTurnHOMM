package oskarinio143.heroes3.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Order(1)
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Opakowujemy oryginalne żądanie naszym wrapperem
        XssRequestWrapper wrappedRequest = new XssRequestWrapper((HttpServletRequest) request);

        // Przekazujemy dalej opakowane żądanie zamiast oryginalnego
        chain.doFilter(wrappedRequest, response);
    }
}
