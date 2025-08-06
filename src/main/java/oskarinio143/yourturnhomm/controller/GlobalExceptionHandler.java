package oskarinio143.yourturnhomm.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.yourturnhomm.exception.*;
import oskarinio143.yourturnhomm.model.constant.Route;
import oskarinio143.yourturnhomm.service.ExceptionMessageCreator;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException exception, RedirectAttributes attributes){
        attributes.addFlashAttribute("duplicateMessage", exception.getMessage());
        return Route.REDIRECT + Route.DATABASE + Route.ADD;
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
