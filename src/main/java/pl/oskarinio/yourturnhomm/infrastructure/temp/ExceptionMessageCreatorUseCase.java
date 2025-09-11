package pl.oskarinio.yourturnhomm.infrastructure.temp;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreatorUseCase {
    String createMessageValidError(BindingResult bindingResult);
}
