package pl.oskarinio.yourturnhomm.infrastructure.port.communication;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreator {
    String createMessageValidError(BindingResult bindingResult);
}
