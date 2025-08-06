package oskarinio143.yourturnhomm.exception;

import org.springframework.transaction.TransactionSystemException;

public class TransactionSystemAddException extends TransactionSystemException {
    public TransactionSystemAddException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
