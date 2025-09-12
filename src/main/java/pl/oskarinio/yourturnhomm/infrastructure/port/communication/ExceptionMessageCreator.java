package pl.oskarinio.yourturnhomm.infrastructure.temp;

import org.springframework.validation.BindingResult;

public interface ExceptionMessageCreator {
    String createMessageValidError(BindingResult bindingResult);
}
