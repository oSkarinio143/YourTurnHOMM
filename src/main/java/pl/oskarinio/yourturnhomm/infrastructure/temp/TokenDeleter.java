package pl.oskarinio.yourturnhomm.infrastructure.temp;

import java.time.Clock;
import java.time.Instant;


public class TokenDeleter {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final Clock clock;

    public TokenDeleter(RefreshTokenRepositoryPort refreshTokenRepositoryPort, Clock clock) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.clock = clock;
    }

    public void cleanExpiredTokens() {
        refreshTokenRepositoryPort.deleteExpiredToken(Instant.now(clock));
    }
}
