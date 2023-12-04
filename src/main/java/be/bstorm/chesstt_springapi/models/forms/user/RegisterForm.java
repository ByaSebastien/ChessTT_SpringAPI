package be.bstorm.chesstt_springapi.models.forms.user;

import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.UserGender;
import be.bstorm.chesstt_springapi.models.enums.UserRole;
import be.bstorm.chesstt_springapi.validators.annotations.BeforeToday;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterForm(

        @NotBlank
        @Size(max = 50)
        String username,

        @NotBlank
        @Size(max = 100)
        String email,

        @BeforeToday
        LocalDate birthdate,

        @Min(0)
        @Max(3000)
        Integer elo,

        @NotNull
        UserGender gender,

        @NotNull
        UserRole role
) {

    public User toEntity(){

        return new User(
                this.username,
                this.email,
                this.birthdate,
                this.elo,
                this.gender,
                this.role
        );
    }
}
