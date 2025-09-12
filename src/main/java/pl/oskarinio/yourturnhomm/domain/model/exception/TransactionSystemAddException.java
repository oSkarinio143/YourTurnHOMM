package pl.oskarinio.yourturnhomm.domain.model.exception;

public class TransactionSystemAddException extends RuntimeException {
    public TransactionSystemAddException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
