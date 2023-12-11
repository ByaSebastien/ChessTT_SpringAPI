package be.bstorm.chesstt_springapi.models.forms.user;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(

        @NotBlank
        String login,

        @NotBlank
        String password) {
}
