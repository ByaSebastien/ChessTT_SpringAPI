package be.bstorm.chesstt_springapi.exceptions.tournament;

public class TournamentException extends RuntimeException{

    public TournamentException(String message) {
        super(message);
    }
}
