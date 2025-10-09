package pl.oskarinio.yourturnhomm.infrastructure.port.communication;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreator {
    String createErrorMessage(BindingResult bindingResult);
}
