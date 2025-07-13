package oskarinio143.heroes3.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.exception.*;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.service.ExceptionMessageCreator;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionMessageCreator exceptionHandlerService;

    public GlobalExceptionHandler(ExceptionMessageCreator exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException exception, RedirectAttributes attributes){
        attributes.addFlashAttribute("duplicateMessage", exception.getMessage());
        return "redirect:/oskarinio143/heroes/database/add";
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
