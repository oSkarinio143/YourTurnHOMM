package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import pl.oskarinio.yourturnhomm.domain.model.Route;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
class MainController {
    @GetMapping()
    public String welcome(HttpServletRequest request, Model model){
        log.info("Uzytkownik w menu glownym");
        Object errorMessage = request.getAttribute("adminErrorMessage");
        if (errorMessage != null)
            model.addAttribute("adminErrorMessage", errorMessage.toString());
        return Route.VIEW_MODE;
    }
}
