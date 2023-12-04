package be.bstorm.chesstt_springapi.validators;


import be.bstorm.chesstt_springapi.validators.annotations.GreaterOrEqualThan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class GreaterOrEqualValidator implements ConstraintValidator<GreaterOrEqualThan, Object> {

    private String min;
    private String max;

    @Override
    public void initialize(GreaterOrEqualThan constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Class<?> c = value.getClass();
            Field minField = c.getDeclaredField(this.min);
            Field maxField = c.getDeclaredField(this.max);

            // Assurez-vous que les champs sont accessibles
            minField.setAccessible(true);
            maxField.setAccessible(true);

            Object minValue = minField.get(value);
            Object maxValue = maxField.get(value);

            if(minValue == null || maxValue == null){
                return true;
            }

            if (!(minValue instanceof Comparable) || !(maxValue instanceof Comparable)) {
                throw new IllegalArgumentException("Both fields must implement Comparable interface.");
            }

            // Assurez-vous que les objets sont du même type pour éviter les erreurs de cast
            if (!minValue.getClass().equals(maxValue.getClass())) {
                throw new IllegalArgumentException("Both fields must be of the same type.");
            }

            return ((Comparable) minValue).compareTo(maxValue) <= 0;

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}