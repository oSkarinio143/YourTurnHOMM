package oskarinio143.heroes3.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class ExceptionHandlerService {

    private final MessageSource messageSource;

    public ExceptionHandlerService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void passDataDuel(RedirectAttributes attributes, HttpServletRequest request){
        attributes.addAttribute("leftUnit", request.getParameter("leftUnit"));
        attributes.addAttribute("rightUnit", request.getParameter("rightUnit"));
        attributes.addAttribute("leftHeroAttack", request.getParameter("leftHeroAttack"));
        attributes.addAttribute("leftHeroDefense", request.getParameter("leftHeroDefense"));
        attributes.addAttribute("rightHeroAttack", request.getParameter("rightHeroAttack"));
        attributes.addAttribute("rightHeroDefense", request.getParameter("rightHeroDefense"));

        if(request.getParameter("leftQuantity") != null)
            attributes.addFlashAttribute("leftQuantity", request.getParameter("leftQuantity"));
        if(request.getParameter("rightQuantity") != null)
            attributes.addFlashAttribute("rightQuantity", request.getParameter("rightQuantity"));
    }

    public String createMessageValidError(BindingResult bindingResult){
        StringBuilder message = new StringBuilder();
        messageAddFieldErrors(bindingResult,message);
        messageAddObjectErrors(bindingResult,message);
        return message.toString();
    }

    private void messageAddFieldErrors(BindingResult bindingResult, StringBuilder message){
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(messageSource.getMessage(error, LocaleContextHolder.getLocale()) + "<br>");
        }
    }

    private void messageAddObjectErrors(BindingResult bindingResult, StringBuilder message){
        for (ObjectError error : bindingResult.getAllErrors()) {
            if(error.getCode().equals("ValidDamageRange"))
                message.append(error.getDefaultMessage());
        }
    }
}
