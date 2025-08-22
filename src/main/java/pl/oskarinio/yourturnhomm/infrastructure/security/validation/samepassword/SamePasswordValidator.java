package pl.oskarinio.yourturnhomm.infrastructure.security.validation.samepassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.RegisterForm;

public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, RegisterForm> {
    @Override
    public boolean isValid(RegisterForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        if(loginForm.getPassword().equals(loginForm.getConfirmPassword()))
            return true;
        return false;
    }
}
