package pl.oskarinio.yourturnhomm.domain.service.rest;

import pl.oskarinio.yourturnhomm.app.rest.port.out.RefreshTokenRepositoryPort;

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
