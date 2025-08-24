package pl.oskarinio.yourturnhomm.app.port.out;

import java.time.Instant;

public interface RefreshTokenRepositoryPort{
    void deleteExpiredToken(Instant date);

}
