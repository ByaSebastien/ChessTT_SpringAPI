package be.bstorm.chesstt_springapi.exceptions.tournament;

public class TournamentValidationException extends TournamentException{

    public TournamentValidationException() {
        super("Tournament is not valid");
    }

    public TournamentValidationException(String message) {
        super(message);
    }
}
