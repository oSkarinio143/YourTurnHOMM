package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.oskarinio.yourturnhomm.domain.model.Route;

//Kontroller do przechwytywania błędów
@Slf4j
@Controller
class CustomErrorController implements ErrorController {

    @RequestMapping(Route.ERROR)
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.error("Wystapil blad - {}", status);
        return Route.REDIRECT;
    }
}
