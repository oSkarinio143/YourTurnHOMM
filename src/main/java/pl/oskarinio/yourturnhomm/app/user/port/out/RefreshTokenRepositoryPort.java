package pl.oskarinio.yourturnhomm.app.user.port.out;

import java.time.Instant;

public interface RefreshTokenRepositoryPort{
    void deleteExpiredToken(Instant date);
}
