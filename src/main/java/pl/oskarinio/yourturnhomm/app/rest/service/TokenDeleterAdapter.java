package pl.oskarinio.yourturnhomm.app.rest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.rest.port.in.TokenDeleterUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.out.RefreshTokenRepositoryPort;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.service.rest.TokenDeleter;

import java.time.Clock;

@Component
public class TokenDeleterAdapter implements TokenDeleterUseCase {
    private final TokenDeleter tokenDeleter;

    @Autowired
    public TokenDeleterAdapter(RefreshTokenRepositoryPort refreshTokenRepositoryPort, UserRepositoryPort userRepository, Clock clock) {
        this.tokenDeleter = new TokenDeleter(refreshTokenRepositoryPort, userRepository, clock);
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void cleanExpiredTokens() {
        tokenDeleter.cleanExpiredTokens();
    }
}