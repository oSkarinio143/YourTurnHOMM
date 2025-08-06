package oskarinio143.yourturnhomm.filter.bearertoken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import oskarinio143.yourturnhomm.model.constant.Route;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        response.sendRedirect(Route.MAIN + Route.LOGIN);
    }
}