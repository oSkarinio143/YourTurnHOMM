package pl.oskarinio.yourturnhomm.domain.model.exception;

public class DuplicateUnitException extends RuntimeException {
    public DuplicateUnitException(String message) {
        super(message);
    }
}
