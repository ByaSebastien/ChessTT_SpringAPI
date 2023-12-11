package be.bstorm.chesstt_springapi.exceptions.tournament;

public class TournamentRegistrationException extends TournamentException{

    public TournamentRegistrationException() {
        super("Cannot register to this tournament");
    }

    public TournamentRegistrationException(String message) {
        super(message);
    }
}
