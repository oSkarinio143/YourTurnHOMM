package oskarinio143.heroes3.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.exception.TransactionSystemAddException;
import oskarinio143.heroes3.model.Unit;

import java.util.Arrays;
import java.util.List;

@Service
public class ExceptionHandlerService {
    public boolean isEmptyValue(MethodArgumentTypeMismatchException exception){
        String value = exception.getValue().toString();
        if(value.equals(""))
            return true;
        return false;
    }

    public String checkEndpoint(HttpServletRequest request){
        String path = request.getRequestURL().toString();
        List<String> pathParts = Arrays.asList(path.split("/"));
        return pathParts.getLast();
    }

    public String getAppropriateUrl(RedirectAttributes attributes, HttpServletRequest request){
        if(checkEndpoint(request).equals("add")) {
            return "redirect:/oskarinio143/heroes/database/add";
        }
        if(checkEndpoint(request).equals("unit")) {
            attributes.addAttribute("name", request.getParameter("name"));
            return "redirect:/oskarinio143/heroes/database/modify/unit";
        }
        return "redirect:/oskarinio143/heroes";
    }

    public ConstraintViolation<?> getViolation(TransactionSystemException exception){
        Throwable cause = exception.getCause();
        ConstraintViolationException newCause = (ConstraintViolationException) cause.getCause();
        List<ConstraintViolation<?>> violations =  newCause.getConstraintViolations().stream().toList();
        return violations.getFirst();
    }

    public boolean isMaxDmgCorrect(ConstraintViolation<?> violation){
        if(violation.getInvalidValue() instanceof Unit unit){
            if(unit.getMaxDamage() < unit.getMinDamage())
                return false;
        }
        return true;
    }

    public void createMessageEmptyField(MethodArgumentTypeMismatchException exception, RedirectAttributes attributes){
        String field = exception.getParameter().getParameterName();
        String message = "Pole " + field + " nie zostalo uzupelnione, spróbuj ponownie";
        attributes.addFlashAttribute("nullMessage", message);
    }

    public void createMessageIncorrectValue(ConstraintViolation<?> violation, RedirectAttributes attributes){
        if(isMaxDmgCorrect(violation))
            createMessageNegativeValue(violation, attributes);
        else
            createMessageTooSmallDmg(violation, attributes);
    }

    public void createMessageNegativeValue(ConstraintViolation<?> violation, RedirectAttributes attributes){
        String message = "Podano wartość " + violation.getInvalidValue() + " w polu " +  violation.getPropertyPath()
                + "<br>Pole " + violation.getMessage();
        attributes.addFlashAttribute("incorrectMessage", message);
    }

    public void createMessageTooSmallDmg(ConstraintViolation<?> violation, RedirectAttributes attributes){
        Unit unit = (Unit) violation.getInvalidValue();
        String message = "Podano wartość " + unit.getMaxDamage() + " w polu MaxDamage"
                + "<br>" + violation.getMessage();
        attributes.addFlashAttribute("incorrectMessage", message);
    }
}
