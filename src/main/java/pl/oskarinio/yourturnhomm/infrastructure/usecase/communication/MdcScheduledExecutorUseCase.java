package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MdcScheduledExecutorUseCase {
    private final ScheduledExecutorService delegate;

    public MdcScheduledExecutorUseCase(ScheduledExecutorService delegate) {
        this.delegate = delegate;
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return delegate.schedule(() -> {
            if (contextMap != null) MDC.setContextMap(contextMap);
            try {
                command.run();
            } finally {
                MDC.clear();
            }
        }, delay, unit);
    }
}
