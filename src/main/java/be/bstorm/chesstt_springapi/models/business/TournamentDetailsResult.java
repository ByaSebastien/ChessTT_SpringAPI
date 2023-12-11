package be.bstorm.chesstt_springapi.models.business;

import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class TournamentDetailsResult extends TournamentResult{

    public boolean canStart;
    public boolean canValidateRound;
    public Set<Match> matches;

    public TournamentDetailsResult(Tournament tournament) {
        super(tournament);
    }
}
