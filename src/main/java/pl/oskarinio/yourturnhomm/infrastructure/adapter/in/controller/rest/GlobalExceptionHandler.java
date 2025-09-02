package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.rest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.DuplicateUnitException;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.UsernameNotMatchingPassword;
import pl.oskarinio.yourturnhomm.domain.model.Route;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException exception, RedirectAttributes attributes){
        attributes.addFlashAttribute("duplicateMessage", exception.getMessage());
        return Route.REDIRECT + Route.ADMIN + Route.DATABASE + Route.ADD;
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(RedirectAttributes redirectAttributes, UsernameNotFoundException usernameNotFoundException) {
        redirectAttributes.addFlashAttribute("errorMessage", "Uzytkownik " + usernameNotFoundException.getMessage() + " nie istnieje w bazie danych");
        return Route.REDIRECT + Route.LOGIN;
    }

    @ExceptionHandler (UsernameNotMatchingPassword.class)
    public String handleUsernameNotMatchingPassword(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "Nazwa uzytkownika i haslo nie pasuja do siebie");
        return Route.REDIRECT + Route.LOGIN;
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "Użytkownik istnieje już w bazie danych");
        return Route.REDIRECT + Route.REGISTER;
    }
}
