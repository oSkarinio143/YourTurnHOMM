package pl.oskarinio.yourturnhomm.app.port.out;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreatorUseCase {
    String createMessageValidError(BindingResult bindingResult);
}
