package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pl.oskarinio.yourturnhomm.app.technology.communication.ExceptionMessageCreatorService;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.ExceptionMessageCreator;

@Service
public class ExceptionMessageCreatorUseCase implements ExceptionMessageCreator {
    private final ExceptionMessageCreatorService exceptionMessageCreatorService;

    public ExceptionMessageCreatorUseCase(MessageSource messageSource) {
        this.exceptionMessageCreatorService = new ExceptionMessageCreatorService(messageSource);
    }

    @Override
    public String createMessageValidError(BindingResult bindingResult) {
        return exceptionMessageCreatorService.createMessageValidError(bindingResult);
    }
}