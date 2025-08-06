package oskarinio143.yourturnhomm.validation.samepassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import oskarinio143.yourturnhomm.model.form.RegisterForm;

public class SamePasswordValidator implements ConstraintValidator<ValidSamePassword, RegisterForm> {
    @Override
    public boolean isValid(RegisterForm loginForm, ConstraintValidatorContext constraintValidatorContext) {
        if(loginForm.getPassword().equals(loginForm.getConfirmPassword()))
            return true;
        return false;
    }
}
