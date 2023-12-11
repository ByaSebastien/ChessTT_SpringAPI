package be.bstorm.chesstt_springapi.models.forms.tournament;

import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;

import java.util.Set;

public record TournamentSearchForm(
        String name,
        Set<TournamentCategory> categories,
        TournamentStatus status,
        Boolean isWomenOnly,
        Integer page
) {
}
