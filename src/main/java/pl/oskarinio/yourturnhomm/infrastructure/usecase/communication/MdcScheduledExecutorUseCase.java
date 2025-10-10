package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//Opakowanie dla ScheduledExecutorService, które zawiera Mdc wątku, z którego został uruchomiony
public class MdcScheduledExecutorUseCase {
    private final ScheduledExecutorService scheduler;

    public MdcScheduledExecutorUseCase(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return scheduler.schedule(() -> {
            if (contextMap != null)
                MDC.setContextMap(contextMap);
            try {
                command.run();
            } finally {
                MDC.clear();
            }
        }, delay, unit);
    }
}
