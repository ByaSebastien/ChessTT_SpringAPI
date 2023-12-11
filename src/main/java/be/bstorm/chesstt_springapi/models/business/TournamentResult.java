package be.bstorm.chesstt_springapi.models.business;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import lombok.Data;

@Data
public class TournamentResult {
    private Tournament tournament;
    private boolean isRegistered;
    private boolean canRegister;

    public TournamentResult(Tournament tournament) {
        this.tournament = tournament;
    }
}
