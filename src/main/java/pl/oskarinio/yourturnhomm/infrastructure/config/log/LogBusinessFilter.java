package pl.oskarinio.yourturnhomm.infrastructure.config.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluatorBase;
import org.slf4j.MDC;

public class LogBusinessFilter extends EventEvaluatorBase {
    private static final String MY_PACKAGE = "pl.oskarinio.yourturnhomm";
    @Override
    public boolean evaluate(Object o) throws NullPointerException {
        LoggingEvent log = (LoggingEvent) o;
        return log.getLoggerName().startsWith(MY_PACKAGE) && MDC.get("traceId") != null;
    }
}
