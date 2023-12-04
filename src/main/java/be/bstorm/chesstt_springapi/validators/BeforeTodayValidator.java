package be.bstorm.chesstt_springapi.validators;

import be.bstorm.chesstt_springapi.validators.annotations.BeforeToday;
import jakarta.persistence.Temporal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class BeforeTodayValidator implements ConstraintValidator<BeforeToday, ChronoLocalDate> {

    @Override
    public void initialize(BeforeToday constraintAnnotation) {
    }

    @Override
    public boolean isValid(ChronoLocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isBefore(LocalDate.now());
    }
}