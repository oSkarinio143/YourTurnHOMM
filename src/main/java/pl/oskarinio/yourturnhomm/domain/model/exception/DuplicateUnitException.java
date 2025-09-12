package pl.oskarinio.yourturnhomm.domain.exception;

public class DuplicateUnitException extends RuntimeException {
    public DuplicateUnitException(String message) {
        super(message);
    }
}
