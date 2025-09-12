package pl.oskarinio.yourturnhomm.infrastructure.usecase.database;

import pl.oskarinio.yourturnhomm.infrastructure.port.database.RefreshTokenRepository;

import java.time.Clock;
import java.time.Instant;


public class TokenDeleterUseCase {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;

    public TokenDeleterUseCase(RefreshTokenRepository refreshTokenRepository, Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.clock = clock;
    }

    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteExpiredToken(Instant.now(clock));
    }
}
