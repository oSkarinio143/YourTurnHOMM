package pl.oskarinio.yourturnhomm.infrastructure.security.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
