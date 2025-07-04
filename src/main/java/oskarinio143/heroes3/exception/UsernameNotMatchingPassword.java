package oskarinio143.heroes3.exception;

public class UsernameNotMatchingPassword extends RuntimeException {
    public UsernameNotMatchingPassword(String message) {
        super(message);
    }

    public UsernameNotMatchingPassword() {}
}
