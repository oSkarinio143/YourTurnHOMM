package pl.oskarinio.yourturnhomm.app.technical.cookie;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.port.RefreshTokenRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.port.cookie.TokenDeleter;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.cookie.TokenDeleterUseCase;

import java.time.Clock;

@Component
public class TokenDeleterService implements TokenDeleter {
    private final TokenDeleterUseCase tokenDeleterUseCase;

    @Autowired
    public TokenDeleterService(RefreshTokenRepositoryPort refreshTokenRepositoryPort, Clock clock) {
        this.tokenDeleterUseCase = new TokenDeleterUseCase(refreshTokenRepositoryPort, clock);
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void cleanExpiredTokens() {
        tokenDeleterUseCase.cleanExpiredTokens();
    }
}