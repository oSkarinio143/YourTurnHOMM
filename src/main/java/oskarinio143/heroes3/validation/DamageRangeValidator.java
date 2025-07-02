package oskarinio143.heroes3.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import oskarinio143.heroes3.model.entity.Unit;

public class DamageRangeValidator implements ConstraintValidator<ValidDamageRange, Unit> {

    @Override
    public boolean isValid(Unit unit, ConstraintValidatorContext context) {
        if (unit.getMinDamage() == null || unit.getMaxDamage() == null) {
            return true;
        }
        return unit.getMaxDamage() >= unit.getMinDamage();
    }
}
