package be.bstorm.chesstt_springapi.models.dtos.tournament;

import java.util.List;

public record TournamentIndexDTO(

        long total,
        List<TournamentDTO> results
) {
}
