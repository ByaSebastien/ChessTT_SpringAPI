package be.bstorm.chesstt_springapi.exceptions.tournament;

public class TournamentInProgressException extends TournamentException{

    public TournamentInProgressException() {
        super("Cannot delete a tournament in progress");
    }

    public TournamentInProgressException(String message) {
        super(message);
    }
}
