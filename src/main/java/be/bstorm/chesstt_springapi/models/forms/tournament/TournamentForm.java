package be.bstorm.chesstt_springapi.models.forms.tournament;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.validators.annotations.EndOfRegistration;
import be.bstorm.chesstt_springapi.validators.annotations.GreaterOrEqualThan;
import be.bstorm.chesstt_springapi.validators.annotations.GreaterOrEqualThans;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

@EndOfRegistration
@GreaterOrEqualThans({
        @GreaterOrEqualThan(min = "minPlayer",max = "maxPlayer"),
        @GreaterOrEqualThan(min = "minElo",max = "maxElo")
})
public record TournamentForm(

        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 100)
        String location,

        @Min(2)
        int minPlayer,

        @Min(2)
        int maxPlayer,

        @Min(0)
        @Max(3000)
        Integer minElo,

        @Min(0)
        @Max(3000)
        Integer maxElo,

        @NotNull
        Set<TournamentCategory> categories,

        @NotNull
        boolean isWomenOnly,

        @NotNull
        LocalDateTime endOfRegistrationDate
) {

    public Tournament toEntity() {

        return new Tournament(
                this.name,
                this.location,
                this.minPlayer,
                this.maxPlayer,
                this.minElo,
                this.maxElo,
                this.categories,
                this.isWomenOnly,
                this.endOfRegistrationDate
        );
    }
}
