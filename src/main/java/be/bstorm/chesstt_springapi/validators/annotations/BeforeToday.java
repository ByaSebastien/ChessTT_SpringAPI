package be.bstorm.chesstt_springapi.validators.annotations;

import be.bstorm.chesstt_springapi.validators.BeforeTodayValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BeforeTodayValidator.class)
public @interface BeforeToday {
    String message() default "La date doit être antérieure à aujourd'hui.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}