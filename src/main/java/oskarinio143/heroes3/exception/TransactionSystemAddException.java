package oskarinio143.heroes3.exception;

import org.springframework.transaction.TransactionSystemException;

public class TransactionSystemAddException extends TransactionSystemException {
    public TransactionSystemAddException(String message) {
        super(message);
    }

    public TransactionSystemAddException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
