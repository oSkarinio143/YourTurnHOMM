package oskarinio143.heroes3.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(){}
}
