package pl.oskarinio.yourturnhomm.domain.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {}

    public UsernameNotFoundException(String message) {
        super(message);
    }

}
