package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotMatchingPassword;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException exception, RedirectAttributes attributes){
        log.warn("Dodanie jednostki nieudane - jednostka juz istnieje");
        attributes.addFlashAttribute("duplicateMessage", exception.getMessage());
        return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.ADD;
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(RedirectAttributes redirectAttributes, UsernameNotFoundException usernameNotFoundException) {
        log.warn("Logowanie nieudane - brak uzytnowika w bazie");
        redirectAttributes.addFlashAttribute("errorMessage", "Uzytkownik " + usernameNotFoundException.getMessage() + " nie istnieje w bazie danych");
        return Route.REDIRECT + Route.LOGIN;
    }

    @ExceptionHandler (UsernameNotMatchingPassword.class)
    public String handleUsernameNotMatchingPassword(RedirectAttributes redirectAttributes){
        log.warn("Logowanie nieudane - uzytkownik podal niepoprawne haslo");
        redirectAttributes.addFlashAttribute("errorMessage", "Nazwa uzytkownika i haslo nie pasuja do siebie");
        return Route.REDIRECT + Route.LOGIN;
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(RedirectAttributes redirectAttributes){
        log.warn("Rejestracja nieudana - uzytkownik istnieje juz w bazie");
        redirectAttributes.addFlashAttribute("errorMessage", "Użytkownik istnieje już w bazie danych");
        return Route.REDIRECT + Route.REGISTER;
    }
}
