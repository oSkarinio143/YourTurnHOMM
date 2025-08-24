package pl.oskarinio.yourturnhomm.app.port.in.rest;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreatorUseCase {
    String createMessageValidError(BindingResult bindingResult);
}
