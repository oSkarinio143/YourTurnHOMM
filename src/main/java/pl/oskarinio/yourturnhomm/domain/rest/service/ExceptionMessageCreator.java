package pl.oskarinio.yourturnhomm.domain.rest.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Component
public class ExceptionMessageCreator {

    private final MessageSource messageSource;

    public ExceptionMessageCreator(MessageSource messageSource) {
        this.messageSource = messageSource;
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
