package pl.oskarinio.yourturnhomm.infrastructure.security.filter.xss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class XssRequestWrapperTest {
    private MockHttpServletRequest request;
    private static final String TEST_PARAMETER = "testParameter";
    private static final String TEST_CORRECT_VALUE = "testValue";
    private String TEST_UNCLEAR_VALUE;

    private XssRequestWrapper xssRequestWrapper;

    @BeforeEach
    void setUp() {
        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();

        xssRequestWrapper = new XssRequestWrapper(request);
    }

    @Test
    @DisplayName("Parametr jest czysty, zwraca tą samą wartość")
    void getParameterValues_setClearParameter_returnSameParamter(){
        request.setParameters(Map.of(TEST_PARAMETER, TEST_CORRECT_VALUE));

        String[] parameterValues = xssRequestWrapper.getParameterValues(TEST_PARAMETER);

        assertThat(parameterValues[0]).isEqualTo(TEST_CORRECT_VALUE);
    }

    @Test
    @DisplayName("Parametr nie jest czysty, oczyszcza go i zwraca")
    void getParameterValues_setUnclearParameter_retrunClearParameter(){
        request.setParameters(Map.of(TEST_PARAMETER, "<script>testValue</script>"));

        String[] parameterValues = xssRequestWrapper.getParameterValues(TEST_PARAMETER);

        assertThat(parameterValues[0]).doesNotContain("script");
    }

    @Test
    @DisplayName("Tablica parametrów, oczyszcza nieczyste i zwraca")
    void getParameterValues_setParametersTable_retrunClearParameters(){
        String[] parameters = {"<script>testValue</script>", TEST_CORRECT_VALUE, "<img>"};
        request.setParameters(Map.of(TEST_PARAMETER, parameters));

        String[] parameterValues = xssRequestWrapper.getParameterValues(TEST_PARAMETER);

        assertThat(parameterValues[0]).doesNotContain("script");
        assertThat(parameterValues[1]).isEqualTo(TEST_CORRECT_VALUE);
        assertThat(parameterValues[2]).doesNotContain("img");
    }

    @Test
    @DisplayName("Dla nieistniejacego parametru zwraca null")
    void getParameterValues_setParameterNull_returnNull(){
        String[] parameterValues = xssRequestWrapper.getParameterValues("nullValues");
        assertThat(parameterValues).isNull();
    }
}
