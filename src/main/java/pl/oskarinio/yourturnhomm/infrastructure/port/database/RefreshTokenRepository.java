package pl.oskarinio.yourturnhomm.infrastructure.port;

import java.time.Instant;

public interface RefreshTokenRepositoryPort {
    void deleteExpiredToken(Instant date);
}
