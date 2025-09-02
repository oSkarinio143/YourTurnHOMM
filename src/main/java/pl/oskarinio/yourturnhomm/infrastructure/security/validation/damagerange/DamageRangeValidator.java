package pl.oskarinio.yourturnhomm.infrastructure.security.validation.damagerange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UnitEntity;

public class DamageRangeValidator implements ConstraintValidator<ValidDamageRange, UnitEntity> {

    @Override
    public boolean isValid(UnitEntity unitEntity, ConstraintValidatorContext constraintValidatorContext) {
        return unitEntity.getMaxDamage() >= unitEntity.getMinDamage();
    }
}
