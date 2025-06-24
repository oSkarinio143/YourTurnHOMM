package oskarinio143.heroes3.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DamageRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDamageRange {
    String message() default "maxDamage nie może być mniejszy niż minDamage";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}