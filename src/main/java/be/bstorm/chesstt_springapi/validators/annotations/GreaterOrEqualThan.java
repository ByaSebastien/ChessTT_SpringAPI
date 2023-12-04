package be.bstorm.chesstt_springapi.validators.annotations;

import be.bstorm.chesstt_springapi.validators.GreaterOrEqualValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Constraint(validatedBy = GreaterOrEqualValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GreaterOrEqualThan {

    String message() default "{max} must be greater or equal than {min}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String min();

    String max();
}
