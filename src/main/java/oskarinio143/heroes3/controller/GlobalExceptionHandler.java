package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.exception.DuplicateUnitException;
import oskarinio143.heroes3.exception.TransactionSystemAddException;
import oskarinio143.heroes3.exception.TransactionSystemModifyException;
import oskarinio143.heroes3.service.ExceptionHandlerService;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final ExceptionHandlerService exceptionHandlerService;

    public GlobalExceptionHandler(ExceptionHandlerService exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @ExceptionHandler(DuplicateUnitException.class)
    public String handleDuplicateUnitException(DuplicateUnitException exception, RedirectAttributes attributes){
        attributes.addFlashAttribute("duplicateMessage", exception.getMessage());
        return "redirect:/oskarinio143/heroes/database/add";
    }

    @ExceptionHandler(TransactionSystemAddException.class)
    public String handleTransactionSystemAddException(TransactionSystemException exception, RedirectAttributes attributes){
        ConstraintViolation<?> violation = exceptionHandlerService.getViolation(exception);
        exceptionHandlerService.createMessageIncorrectValue(violation, attributes);
        return "redirect:/oskarinio143/heroes/database/add";
    }

    @ExceptionHandler(TransactionSystemModifyException.class)
    public String handleTransactionSystemModifyException(TransactionSystemException exception, RedirectAttributes attributes){
        ConstraintViolation<?> violation = exceptionHandlerService.getViolation(exception);
        exceptionHandlerService.createMessageIncorrectValue(violation, attributes);
        attributes.addAttribute("name", exception.getMessage());
        return "redirect:/oskarinio143/heroes/database/modify/unit";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, RedirectAttributes attributes, HttpServletRequest request){
        exceptionHandlerService.createMessageEmptyField(exception, attributes);
        if(exceptionHandlerService.isEmptyValue(exception))
            return exceptionHandlerService.getAppropriateUrl(attributes, request);
        return "redirect:/oskarinio143/heroes";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleArgumentNotValidException(MethodArgumentNotValidException exception, RedirectAttributes attributes, HttpServletRequest request){
        if(exceptionHandlerService.isBattleEndpoint(request))
            exceptionHandlerService.createMessageTooSmallValue(attributes);
        exceptionHandlerService.passData(attributes, request);
        return "redirect:/oskarinio143/heroes/duel";
    }
}
