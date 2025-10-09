package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionMessageCreatorUseCaseTest {
    @Mock
    private MessageSource messageSource;
    private BindingResult bindingResult;
    private FieldError fieldError;
    private ObjectError trueObjectError;
    private ObjectError falseObjectError;

    private ExceptionMessageCreatorUseCase exceptionMessageCreatorUseCase;

    @BeforeEach
    void setUp(){
        exceptionMessageCreatorUseCase = new ExceptionMessageCreatorUseCase(messageSource);

        bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        fieldError = new FieldError("testError", "testField", "testFieldMessage");
        trueObjectError = new ObjectError("testError", new String[]{"ValidDamageRange"}, null, "testTrueObjectMessage");
        falseObjectError = new ObjectError("testError", new String[]{"falseMessage"}, null, "testFalseObjectMessage");
    }

    @Test
    @DisplayName("Pusty BindingResult, zwraca pustą wiadomość")
    void createErrorMessage_emptyBindingResult_resultBlankMessage(){
        String message = exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);

        assertThat(message).isBlank();
    }

    @Test
    @DisplayName("BindingResult z objectError zawierającym ValidDamageRange, zwraca w wiadomości treść błędu")
    void createErrorMessage_objectErrorWithTrueMessage_resultMessageWithErrorMessage(){
        bindingResult.addError(trueObjectError);

        String message = exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);

        assertThat(message).isEqualTo(trueObjectError.getDefaultMessage());
    }

    @Test
    @DisplayName("BindingResult z objectError zawierającym losowy tekst, zwraca pustą wiadomość")
    void createErrorMessage_objectErrorWithFalseMessage_resultBlankMessage(){
        bindingResult.addError(falseObjectError);

        String message = exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);

        assertThat(message).isBlank();
    }

    @Test
    @DisplayName("BindingResult z fieldError, zwraca w wiadomości treść błędu")
    void createErrorMessage_fieldErrorWithMessage_resultMessageWithErrorMessage(){
        bindingResult.addError(fieldError);

        when(messageSource.getMessage(any(), any())).thenReturn(fieldError.getDefaultMessage());
        String message = exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);

        assertThat(message).isEqualTo(fieldError.getDefaultMessage() + "<br>");
    }

    @Test
    @DisplayName("BindingResult z fieldError i objectError, zwraca w wiadomości tekst błędów")
    void createErrorMessage_fieldObjectErrorsWithTrueMessage_resultMessageWithErrorsMessage(){
        bindingResult.addError(fieldError);
        bindingResult.addError(trueObjectError);

        when(messageSource.getMessage(any(), any())).thenReturn(fieldError.getDefaultMessage());
        String message = exceptionMessageCreatorUseCase.createErrorMessage(bindingResult);

        assertThat(message).isEqualTo(fieldError.getDefaultMessage() + "<br>" + trueObjectError.getDefaultMessage());
    }
}
