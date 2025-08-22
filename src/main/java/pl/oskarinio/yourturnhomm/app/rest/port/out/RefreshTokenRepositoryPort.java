package pl.oskarinio.yourturnhomm.app.rest.port.out;

import java.time.Instant;

public interface RefreshTokenRepositoryPort{
    void deleteExpiredToken(Instant date);

}
