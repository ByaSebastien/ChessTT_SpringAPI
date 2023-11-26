package be.bstorm.chesstt_springapi.exceptions.users;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
