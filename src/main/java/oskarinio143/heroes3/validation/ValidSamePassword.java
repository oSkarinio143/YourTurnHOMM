package oskarinio143.heroes3.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SamePasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSamePassword {
    String message() default "Hasła nie mogą się od siebie różnić";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
