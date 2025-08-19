package pl.oskarinio.yourturnhomm.infrastructure.security.filter.bearertoken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("access Denied");
        request.setAttribute("adminErrorMessage", "Nie posiadasz uprawnień administratorskich");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.sendRedirect(Route.MAIN);
    }
}