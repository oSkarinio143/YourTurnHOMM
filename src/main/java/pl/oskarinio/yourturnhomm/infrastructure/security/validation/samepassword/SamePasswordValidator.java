package pl.oskarinio.yourturnhomm.infrastructure.security.validation.samepassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;

public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        return loginForm.getPassword().equals(loginForm.getConfirmPassword());
    }
}
