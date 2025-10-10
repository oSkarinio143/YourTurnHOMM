package pl.oskarinio.yourturnhomm.app.technology.communication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.ExceptionMessageCreator;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.ExceptionMessageCreatorUseCase;

@Slf4j
@Component
public class ExceptionMessageCreatorService implements ExceptionMessageCreator {
    private final ExceptionMessageCreatorUseCase exceptionMessageCreatorUseCase;

    public ExceptionMessageCreatorService(ExceptionMessageCreatorUseCase exceptionMessageCreatorUseCase) {
        this.exceptionMessageCreatorUseCase = exceptionMessageCreatorUseCase;
    }

    @Override
    public String createErrorMessage(BindingResult bindingResult) {
        log.debug("Tworze komunikat bledu walidacji. Ilosc fieldErrors = {}, ilosc objectErrors = {}",
                bindingResult.getFieldErrorCount(), bindingResult.getGlobalErrorCount());
        return exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);
    }
}
