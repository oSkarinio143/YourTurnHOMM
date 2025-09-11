package pl.oskarinio.yourturnhomm.infrastructure.temp;

import java.time.Instant;

public interface RefreshTokenRepositoryPort{
    void deleteExpiredToken(Instant date);

}
