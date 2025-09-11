package pl.oskarinio.yourturnhomm.infrastructure.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.oskarinio.yourturnhomm.domain.model.Route;

@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Ta metoda przechwytuje wszystkie błędy przekierowane przez Spring Boot na ścieżkę /error.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Pobieramy kod statusu HTTP z atrybutów żądania
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // Sprawdzamy, czy błąd to 404 Not Found
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // Jeśli tak, przekierowujemy użytkownika na stronę główną
                return Route.REDIRECT;
            }
        }

        // Dla wszystkich innych błędów (np. 500, 403), możesz
        // zwrócić widok standardowej strony błędu lub również przekierować.
        // Tutaj dla prostoty też przekierowujemy na stronę główną.
        return Route.REDIRECT;
    }
}
