package pl.oskarinio.yourturnhomm.infrastructure.security.validation.damagerange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;

public class DamageRangeValidator implements ConstraintValidator<ValidDamageRange, Unit> {

    @Override
    public boolean isValid(Unit unit, ConstraintValidatorContext context) {
        return unit.getMaxDamage() >= unit.getMinDamage();
    }
}
