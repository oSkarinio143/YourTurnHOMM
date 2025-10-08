package pl.oskarinio.yourturnhomm.infrastructure.config.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.oskarinio.yourturnhomm.infrastructure.config.TestWebUtilities;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.repository.UserRepositoryUseCase;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pl.oskarinio.yourturnhomm.domain.model.user.Role.ROLE_USER;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {
    @Mock
    private Tracer tracer;
    @Mock
    private UserRepositoryUseCase userRepositoryUseCase;

    private static final String TEST_TRACE = "TEST_TRACE";
    private static final String TEST_USER = "TEST_USER";
    private static final String TEST_TRACE_VALUE = "traceId";
    private static final String TEST_USER_VALUE = "userId";
    private static final long TEST_ID = 123;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    private LoggingFilter loggingFilter;

    @BeforeEach
    void SetUp(){
        loggingFilter = new LoggingFilter(tracer, userRepositoryUseCase);

        TestWebUtilities webUtilities = new TestWebUtilities();
        request = webUtilities.getRequest();
        response =  webUtilities.getResponse();
        filterChain = webUtilities.getFilterChain();
    }

    @Test
    @DisplayName("Ustawiam domyślne wartości, metoda sprawdza czy filter poprawnie ustawia userId i traceId")
    void doFilterInternal_correctValues_setCorrectTraceUserId() throws ServletException, IOException {
        doFilterInternal_arrange();
        doFilterInternal_act();
        doFilterInternal_assert();
        doFilterInternal_clear();
    }

    private void doFilterInternal_arrange(){
        Span span = mock(Span.class);
        when(tracer.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(mock(TraceContext.class));
        when(span.context().traceId()).thenReturn(TEST_TRACE);

        UserEntity user = getUser();
        when(userRepositoryUseCase.findByUsername(TEST_USER)).thenReturn(Optional.of(user));
    }

    private void doFilterInternal_act() throws ServletException, IOException {
        Authentication testAuth = new UsernamePasswordAuthenticationToken(
                TEST_USER,
                null,
                List.of(new SimpleGrantedAuthority(ROLE_USER.toString()))
        );
        SecurityContext testContext = SecurityContextHolder.createEmptyContext();
        testContext.setAuthentication(testAuth);
        SecurityContextHolder.setContext(testContext);

        loggingFilter.doFilterInternal(request, response, filterChain);
    }

    private void doFilterInternal_assert() throws ServletException, IOException {
        assertThat(MDC.get(TEST_TRACE_VALUE)).isEqualTo(TEST_TRACE);
        assertThat(MDC.get(TEST_USER_VALUE)).isEqualTo(String.valueOf(TEST_ID));
        verify(filterChain).doFilter(request, response);
    }

    private void doFilterInternal_clear(){
        MDC.clear();
        SecurityContextHolder.clearContext();
    }

    private UserEntity getUser(){
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USER);
        user.setId(TEST_ID);
        return user;
    }
}
