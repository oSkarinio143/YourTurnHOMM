package oskarinio143.heroes3.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import oskarinio143.heroes3.model.RegisterForm;

public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, RegisterForm> {
    @Override
    public boolean isValid(RegisterForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        if(loginForm.getPassword().equals(loginForm.getConfirmPassword()))
            return true;
        return false;
    }
}
