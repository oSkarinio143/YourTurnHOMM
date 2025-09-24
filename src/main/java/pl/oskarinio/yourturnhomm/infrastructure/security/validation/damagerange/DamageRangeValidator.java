package pl.oskarinio.yourturnhomm.infrastructure.security.validation.damagerange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

//MinDmg nie może być większy od MaxDmg
public class DamageRangeValidator implements ConstraintValidator<ValidDamageRange, UnitEntity> {

    @Override
    public boolean isValid(UnitEntity unitEntity, ConstraintValidatorContext constraintValidatorContext) {
        return unitEntity.getMaxDamage() >= unitEntity.getMinDamage();
    }
}
