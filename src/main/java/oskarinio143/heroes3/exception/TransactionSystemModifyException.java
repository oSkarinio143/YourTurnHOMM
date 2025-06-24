package oskarinio143.heroes3.exception;

import org.springframework.transaction.TransactionSystemException;

public class TransactionSystemModifyException extends TransactionSystemException {
    public TransactionSystemModifyException(String message) {
        super(message);
    }

    public TransactionSystemModifyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
