package pl.oskarinio.yourturnhomm.infrastructure.usecase.cookie;

import pl.oskarinio.yourturnhomm.infrastructure.port.RefreshTokenRepositoryPort;

import java.time.Clock;
import java.time.Instant;


public class TokenDeleterUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final Clock clock;

    public TokenDeleterUseCase(RefreshTokenRepositoryPort refreshTokenRepositoryPort, Clock clock) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.clock = clock;
    }

    public void cleanExpiredTokens() {
        refreshTokenRepositoryPort.deleteExpiredToken(Instant.now(clock));
    }
}
