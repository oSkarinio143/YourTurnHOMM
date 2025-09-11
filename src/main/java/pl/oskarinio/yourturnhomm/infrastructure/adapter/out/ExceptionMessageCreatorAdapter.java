package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.oskarinio.yourturnhomm.domain.rest.ExceptionMessageCreator;
import pl.oskarinio.yourturnhomm.infrastructure.temp.ExceptionMessageCreatorUseCase;

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