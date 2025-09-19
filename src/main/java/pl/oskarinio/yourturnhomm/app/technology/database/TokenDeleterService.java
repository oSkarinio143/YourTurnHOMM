package pl.oskarinio.yourturnhomm.app.technology.database;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.port.database.RefreshTokenRepository;
import pl.oskarinio.yourturnhomm.infrastructure.port.database.TokenDeleter;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.TokenDeleterUseCase;

import java.time.Clock;

@Slf4j
@Component
public class TokenDeleterService implements TokenDeleter {
    private final TokenDeleterUseCase tokenDeleterUseCase;

    @Autowired
    public TokenDeleterService(RefreshTokenRepository refreshTokenRepository, Clock clock) {
        this.tokenDeleterUseCase = new TokenDeleterUseCase(refreshTokenRepository, clock);
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void cleanExpiredTokens() {
        log.debug("Uruchamiam czyszczenie tokenow");
        tokenDeleterUseCase.cleanExpiredTokens();
    }
}