package be.bstorm.chesstt_springapi.validators;

import be.bstorm.chesstt_springapi.models.forms.tournament.TournamentForm;
import be.bstorm.chesstt_springapi.validators.annotations.EndOfRegistration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EndOfRegistrationValidator implements ConstraintValidator<EndOfRegistration, TournamentForm> {
    @Override
    public void initialize(EndOfRegistration constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TournamentForm tournamentForm, ConstraintValidatorContext constraintValidatorContext) {

        if(tournamentForm.endOfRegistrationDate() == null){
            return true;
        }

        return !tournamentForm.endOfRegistrationDate().isBefore(LocalDateTime.now().plusDays(tournamentForm.minPlayer()));
    }
}
