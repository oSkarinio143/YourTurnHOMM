package oskarinio143.heroes3.exception;

public class UserAlreadyExistsInDatabase extends RuntimeException {
    public UserAlreadyExistsInDatabase(String message) {
        super(message);
    }

    public UserAlreadyExistsInDatabase() {}
}
