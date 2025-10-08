package pl.oskarinio.yourturnhomm.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class MdcTaskDecoratorTest {
    private static final String TEST_MDC_KEY = "testKey";
    private static final String TEST_MDC_VALUE = "testValue";
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
        Runnable testRunnable = () -> mdcValue.set(MDC.get(TEST_MDC_KEY));
        MDC.setContextMap(Map.of(TEST_MDC_KEY, TEST_MDC_VALUE));
        Runnable decoratedTestRunnable = mdcTaskDecorator.decorate(testRunnable);
        Runnable checkIsMdcAndIsClear = () -> {
            decoratedTestRunnable.run();
            mdcValueAfterClear.set(MDC.get(TEST_MDC_KEY));
        };

        Thread mdcTread = new Thread(checkIsMdcAndIsClear);
        mdcTread.start();
        mdcTread.join();

        assertThat(mdcValue.get()).isEqualTo(TEST_MDC_VALUE);
        assertThat(mdcValueAfterClear.get()).isNull();
    }
}
