package be.bstorm.chesstt_springapi.models.dtos.match;

import be.bstorm.chesstt_springapi.models.enums.MatchResult;
import jakarta.validation.constraints.NotNull;

public record MatchResultDTO(
        @NotNull
        MatchResult result
) {
}
