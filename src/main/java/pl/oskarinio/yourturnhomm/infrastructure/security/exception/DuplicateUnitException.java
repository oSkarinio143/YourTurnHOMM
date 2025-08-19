package pl.oskarinio.yourturnhomm.infrastructure.security.exception;

public class DuplicateUnitException extends RuntimeException {
    public DuplicateUnitException(String message) {
        super(message);
    }
}
