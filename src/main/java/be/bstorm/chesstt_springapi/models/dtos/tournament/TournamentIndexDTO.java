package be.bstorm.chesstt_springapi.models.dtos.tournament;

import java.util.List;

public record TournamentIndexDTO(

        int total,
        List<TournamentDTO> results
) {
}
