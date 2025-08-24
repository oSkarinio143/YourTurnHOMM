package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.port.out.repository.RefreshTokenRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.db.repository.RefreshTokenRepository;

import java.time.Instant;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
    private final RefreshTokenRepository refreshTokenRepostiory;

    public RefreshTokenRepositoryAdapter(RefreshTokenRepository jpaRepository) {
        this.refreshTokenRepostiory = jpaRepository;
    }

    @Override
    public void deleteExpiredToken(Instant date) {
        refreshTokenRepostiory.deleteExpiredToken(date);
    }
}
