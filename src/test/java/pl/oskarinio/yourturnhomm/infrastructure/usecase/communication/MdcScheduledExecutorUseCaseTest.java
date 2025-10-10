package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class MdcScheduledExecutorUseCaseTest {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static final String MDC_KEY = "testKey";
    private static final String MDC_VALUE = "testValue";
    private final AtomicReference<String> mdcValue = new AtomicReference<>();

    private MdcScheduledExecutorUseCase mdcScheduledExecutorUseCase;

    @BeforeEach
    void setUp() {
        mdcScheduledExecutorUseCase = new MdcScheduledExecutorUseCase(scheduler);
    }

    @AfterEach
    void tearDown() {
        scheduler.shutdown();
        MDC.clear();
    }

    @Test
    @DisplayName("MDC ustawione w wątku, ustawiam w innym wątku, rezultat Mdc poprawnie ustawione")
    void schedule_correctMDC_resultMdcSetCorrectly() throws ExecutionException, InterruptedException {
        Runnable runnable = () -> mdcValue.compareAndSet(null, MDC.get(MDC_KEY));
        MDC.setContextMap(Map.of(MDC_KEY, MDC_VALUE));

        assertThat(mdcValue.get()).isNull();
        startThread(runnable);

        assertThat(mdcValue.get()).isEqualTo(MDC_VALUE);
    }

    @Test
    @DisplayName("MDC null, ustawiam w innym wątku, rezultat MDC null")
    void schedule_nullMDC_resultMdcNull() throws ExecutionException, InterruptedException {
        Runnable runnable = () -> mdcValue.compareAndSet(null, MDC.get(MDC_KEY));

        assertThat(mdcValue.get()).isNull();
        startThread(runnable);

        assertThat(mdcValue.get()).isNull();
    }

    @Test
    @DisplayName("MDC ustawione w drugim wątku, czyszczę MDC, rezultat MDC null")
    void schedule_setMdc_clearedMdc() throws ExecutionException, InterruptedException {
        Runnable firstTask = () -> mdcValue.compareAndSet(null, MDC.get(MDC_KEY));
        MDC.setContextMap(Map.of(MDC_KEY, MDC_VALUE));
        startThread(firstTask);
        mdcValue.compareAndSet(MDC_VALUE, null);
        MDC.clear();

        Runnable secondTask = () -> mdcValue.compareAndSet(null, MDC.get(MDC_KEY));
        startThread(secondTask);

        assertThat(mdcValue.get()).isNull();
    }

    private void startThread(Runnable runnable) throws InterruptedException, ExecutionException {
        ScheduledFuture<?> scheduledRunnable = mdcScheduledExecutorUseCase.schedule(runnable, 0, TimeUnit.MILLISECONDS);
        scheduledRunnable.get();
    }
}
