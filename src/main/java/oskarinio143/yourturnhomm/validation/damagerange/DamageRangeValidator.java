package oskarinio143.yourturnhomm.validation.damagerange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import oskarinio143.yourturnhomm.model.entity.Unit;

public class DamageRangeValidator implements ConstraintValidator<ValidDamageRange, Unit> {

    @Override
    public boolean isValid(Unit unit, ConstraintValidatorContext context) {
        return unit.getMaxDamage() >= unit.getMinDamage();
    }
}
