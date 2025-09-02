package pl.oskarinio.yourturnhomm.infrastructure.logger;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppLogger {
    private final CurrentUserService currentUserService;
    private final Tracer tracer;
    private Span currentSpan;
    private String traceId;
    private Long userId ;


    public AppLogger(CurrentUserService currentUserService, Tracer tracer) {
        this.currentUserService = currentUserService;
        this.tracer = tracer;
    }

    public void info(String msg){
        currentSpan = tracer.currentSpan();
        traceId = currentSpan != null ? currentSpan.context().traceId() : "none";
        userId = currentUserService.getCurrentUserId();
        log.info("\nLog: {}; TraceId - {}; UserId - {}", msg, traceId, userId);
    }
}
