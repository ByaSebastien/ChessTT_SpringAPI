package be.bstorm.chesstt_springapi.exceptions.match;

public class MatchNotFoundException extends MatchException{
    public MatchNotFoundException() {
        super("Match not found");
    }

    public MatchNotFoundException(String message) {
        super(message);
    }
}
