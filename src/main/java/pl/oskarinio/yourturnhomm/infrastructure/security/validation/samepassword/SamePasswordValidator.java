package pl.oskarinio.yourturnhomm.infrastructure.security.validation.samepassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterFormRequest;

//Użytkownik musi podać to samo hasło przy rejestracji
public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, RegisterFormRequest> {

    @Override
    public boolean isValid(RegisterFormRequest loginForm, ConstraintValidatorContext constraintValidatorContext) {
        return loginForm.getPassword().equals(loginForm.getConfirmPassword());
    }
}
