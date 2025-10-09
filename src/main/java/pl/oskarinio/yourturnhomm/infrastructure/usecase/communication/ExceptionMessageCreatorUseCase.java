package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.ExceptionMessageCreator;

@Service
public class ExceptionMessageCreatorUseCase implements ExceptionMessageCreator {
    private final MessageSource messageSource;

    public ExceptionMessageCreatorUseCase(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String createErrorMessage(BindingResult bindingResult){
        StringBuilder message = new StringBuilder();
        messageAddFieldErrors(bindingResult, message);
        messageAddObjectErrors(bindingResult, message);
        return message.toString();
    }

    private void messageAddFieldErrors(BindingResult bindingResult, StringBuilder message){
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(messageSource.getMessage(error, LocaleContextHolder.getLocale())).append("<br>");
        }
    }

    private void messageAddObjectErrors(BindingResult bindingResult, StringBuilder message){
        for (ObjectError error : bindingResult.getAllErrors()) {
            if("ValidDamageRange".equals(error.getCode())) {
                message.append(error.getDefaultMessage());
            }
        }
    }
}