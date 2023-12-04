package be.bstorm.chesstt_springapi.exceptions.user;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
