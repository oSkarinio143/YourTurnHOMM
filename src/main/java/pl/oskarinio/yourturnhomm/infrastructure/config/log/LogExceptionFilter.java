package pl.oskarinio.yourturnhomm.infrastructure.config.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class LogExceptionFilter extends EventEvaluatorBase {
    @Override
    public boolean evaluate(Object o) throws NullPointerException, EvaluationException {
        LoggingEvent log = (LoggingEvent) o;
        return log.getThrowableProxy() != null;
    }
}
