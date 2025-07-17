package oskarinio143.heroes3.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.util.UriComponentsBuilder;
import oskarinio143.heroes3.model.constant.Route;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        request.setAttribute("adminErrorMessage", "Nie posiadasz uprawnień administratorskich");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        request.getRequestDispatcher(Route.MAIN).forward(request, response);
    }
}