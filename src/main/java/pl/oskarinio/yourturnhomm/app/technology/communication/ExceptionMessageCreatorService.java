package pl.oskarinio.yourturnhomm.app.technology.communication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Slf4j
@Component
public class ExceptionMessageCreatorService {

    private final MessageSource messageSource;

    public ExceptionMessageCreatorService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String createMessageValidError(BindingResult bindingResult){
        log.debug("Tworze komunikat bledu walidacji. Ilosc fieldErrors = {}, ilosc objectErrors = {}",
                bindingResult.getFieldErrorCount(), bindingResult.getGlobalErrorCount());

        StringBuilder message = new StringBuilder();
        messageAddFieldErrors(bindingResult,message);
        messageAddObjectErrors(bindingResult,message);
        return message.toString();
    }

    private void messageAddFieldErrors(BindingResult bindingResult, StringBuilder message){
        for (FieldError error : bindingResult.getFieldErrors()) {
            log.debug("Dodaje fieldError. Pole = {}, Kod = {}", error.getField(), error.getCode());
            message.append(messageSource.getMessage(error, LocaleContextHolder.getLocale())).append("<br>");
        }
    }

    private void messageAddObjectErrors(BindingResult bindingResult, StringBuilder message){
        for (ObjectError error : bindingResult.getAllErrors()) {
            log.debug("Dodaje objectError. Kod = {}", error.getCode());
            if("ValidDamageRange".equals(error.getCode())) {
                message.append(error.getDefaultMessage());
            }
        }
    }

}
