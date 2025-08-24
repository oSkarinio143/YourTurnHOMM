package pl.oskarinio.yourturnhomm.app.implementation.rest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.oskarinio.yourturnhomm.app.port.in.rest.ExceptionMessageCreatorUseCase;
import pl.oskarinio.yourturnhomm.domain.service.rest.ExceptionMessageCreator;

@Service
public class ExceptionMessageCreatorAdapter implements ExceptionMessageCreatorUseCase {
    private final ExceptionMessageCreator exceptionMessageCreator;

    public ExceptionMessageCreatorAdapter(MessageSource messageSource) {
        this.exceptionMessageCreator = new ExceptionMessageCreator(messageSource);
    }

    @Override
    public String createMessageValidError(BindingResult bindingResult) {
        return exceptionMessageCreator.createMessageValidError(bindingResult);
    }
}