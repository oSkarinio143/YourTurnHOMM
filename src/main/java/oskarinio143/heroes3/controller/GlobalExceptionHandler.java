package oskarinio143.heroes3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.exception.DuplicateUnitException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException ex, RedirectAttributes ra){
        ra.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/oskarinio143/heroes/database/add";
    }
}
