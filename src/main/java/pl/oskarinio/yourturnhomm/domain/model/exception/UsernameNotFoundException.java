package pl.oskarinio.yourturnhomm.domain.model.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {}

    public UsernameNotFoundException(String message) {
        super(message);
    }

}
