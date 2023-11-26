package be.bstorm.chesstt_springapi.exceptions.users;

public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException() {
        super("User already exist");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
