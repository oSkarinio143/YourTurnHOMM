package pl.oskarinio.yourturnhomm.app.user.port.in;

import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface RefreshTokenUseCase {
    void deleteExpiredToken(Instant date);
}
