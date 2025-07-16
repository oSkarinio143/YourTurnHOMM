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
import oskarinio143.heroes3.model.constant.Route;

import java.io.IOException;
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final FlashMapManager flashMapManager;

    public CustomAccessDeniedHandler() {
        this.flashMapManager = new SessionFlashMapManager();
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        FlashMap flashMap = new FlashMap();
        flashMap.put("adminErrorMessage", "Musisz mieć uprawnienia administratora aby móc to zrobić");
        flashMap.setTargetRequestPath(request.getContextPath() + Route.MAIN);
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        response.sendRedirect(Route.MAIN);
    }
}
