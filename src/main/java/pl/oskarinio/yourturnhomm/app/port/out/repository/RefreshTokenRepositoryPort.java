package pl.oskarinio.yourturnhomm.app.port.out.repository;

import java.time.Instant;

public interface RefreshTokenRepositoryPort{
    void deleteExpiredToken(Instant date);

}
