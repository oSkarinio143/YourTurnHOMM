package pl.oskarinio.yourturnhomm.infrastructure.config.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.oskarinio.yourturnhomm.infrastructure.db.entity.UserEntity;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.repository.UserRepositoryUseCase;

import java.io.IOException;
import java.util.Optional;

@Component
class LoggingFilter extends OncePerRequestFilter {

    private final Tracer tracer;
    private final UserRepositoryUseCase userRepositoryUseCase;

    public LoggingFilter(Tracer tracer, UserRepositoryUseCase userRepositoryUseCase) {
        this.tracer = tracer;
        this.userRepositoryUseCase = userRepositoryUseCase;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        setTraceId();
        setUserId();
        filterChain.doFilter(request, response);
    }

    private void setUserId(){
        Long userId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Optional<UserEntity> optUser = userRepositoryUseCase.findByUsername(auth.getName());
            if(optUser.isPresent()) {
                userId = optUser.get().getId();
                MDC.put("userId", userId.toString());
            }
        }
    }

    private void setTraceId(){
        Span currentSpan = tracer.currentSpan();
        String traceId = currentSpan != null ? currentSpan.context().traceId() : "none";
        MDC.put("traceId", traceId);
    }
}
