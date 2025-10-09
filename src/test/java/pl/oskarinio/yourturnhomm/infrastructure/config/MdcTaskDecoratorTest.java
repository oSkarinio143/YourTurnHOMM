package pl.oskarinio.yourturnhomm.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class MdcTaskDecoratorTest {
    private static final String MDC_KEY = "testKey";
    private static final String MDC_VALUE = "testValue";
    private final AtomicReference<String> mdcValue = new AtomicReference<>();
    private final AtomicReference<String> mdcValueAfterClear = new AtomicReference<>();

    private MdcTaskDecorator mdcTaskDecorator;

    @BeforeEach
    void setUp(){
        mdcTaskDecorator = new MdcTaskDecorator();
    }

    @Test
    @DisplayName("Metoda sprawdza czy poprawnie opakowuje wątek z MDC a następnie czyści")
    void decorate_defaultValues() throws InterruptedException {
        Runnable testRunnable = () -> mdcValue.set(MDC.get(MDC_KEY));
        MDC.setContextMap(Map.of(MDC_KEY, MDC_VALUE));
        Runnable decoratedTestRunnable = mdcTaskDecorator.decorate(testRunnable);
        Runnable checkIsMdcAndIsClear = () -> {
            decoratedTestRunnable.run();
            mdcValueAfterClear.set(MDC.get(MDC_KEY));
        };

        startThread(checkIsMdcAndIsClear);

        assertThat(mdcValue.get()).isEqualTo(MDC_VALUE);
        assertThat(mdcValueAfterClear.get()).isNull();
    }

    private void startThread(Runnable runnable) throws InterruptedException {
        Thread mdcTread = new Thread(runnable);
        mdcTread.start();
        mdcTread.join();
    }
}
