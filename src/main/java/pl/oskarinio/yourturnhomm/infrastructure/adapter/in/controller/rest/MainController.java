package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.infrastructure.logger.AppLogger;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
class MainController {
    private final AppLogger appLogger;

    MainController(AppLogger appLogger) {
        this.appLogger = appLogger;
    }

    @GetMapping()
    public String welcome(HttpServletRequest request, Model model){
        appLogger.info("User przeszedl do menu glownego");
        Object errorMessage = request.getAttribute("adminErrorMessage");
        if (errorMessage != null)
            model.addAttribute("adminErrorMessage", errorMessage.toString());
        return Route.VIEW_MODE;
    }
}
