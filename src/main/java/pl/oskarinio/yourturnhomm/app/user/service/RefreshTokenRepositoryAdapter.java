package pl.oskarinio.yourturnhomm.app.user.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.oskarinio.yourturnhomm.app.user.port.out.RefreshTokenRepositoryPort;

import java.sql.Ref;
import java.time.Instant;

public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expirationDate < :date")
    void deleteExpiredToken(Instant date) {
    }
}
