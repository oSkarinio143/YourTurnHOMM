package pl.oskarinio.yourturnhomm.domain.rest.port.in;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreatorUseCase {
    String createMessageValidError(BindingResult bindingResult);
}
