package be.bstorm.chesstt_springapi.exceptions.match;

public class MatchException extends RuntimeException{
    public MatchException(String message) {
        super(message);
    }
}
