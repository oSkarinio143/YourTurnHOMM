package oskarinio143.heroes3.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import oskarinio143.heroes3.model.LoginForm;

public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, LoginForm> {
    @Override
    public boolean isValid(LoginForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        if(loginForm.getPassword().equals(loginForm.getConfirmPassword()))
            return true;
        return false;
    }
}
