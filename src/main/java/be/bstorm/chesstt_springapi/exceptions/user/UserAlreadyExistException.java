package be.bstorm.chesstt_springapi.exceptions.user;

public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException() {
        super("User already exist");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
