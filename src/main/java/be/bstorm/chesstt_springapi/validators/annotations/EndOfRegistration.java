package be.bstorm.chesstt_springapi.validators.annotations;

import be.bstorm.chesstt_springapi.validators.EndOfRegistrationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = EndOfRegistrationValidator.class)
public @interface EndOfRegistration {
    String message() default "End of registration date is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
