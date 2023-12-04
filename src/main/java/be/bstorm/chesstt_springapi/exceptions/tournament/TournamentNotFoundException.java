package be.bstorm.chesstt_springapi.exceptions.tournament;

public class TournamentNotFoundException extends TournamentException{

    public TournamentNotFoundException() {
        super("Tournament not found");
    }

    public TournamentNotFoundException(String message) {
        super(message);
    }
}
