package pl.oskarinio.yourturnhomm.infrastructure.port.database;

import java.time.Instant;

public interface RefreshTokenRepository {
    void deleteExpiredToken(Instant date);
}
