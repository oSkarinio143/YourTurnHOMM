package pl.oskarinio.yourturnhomm.infrastructure.adapter.out.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.RefreshTokenRepository;

import java.time.Instant;

@Component
public class RefreshTokenRepositoryService implements pl.oskarinio.yourturnhomm.infrastructure.port.database.RefreshTokenRepository {
    private final RefreshTokenRepository refreshTokenRepostiory;

    public RefreshTokenRepositoryService(RefreshTokenRepository jpaRepository) {
        this.refreshTokenRepostiory = jpaRepository;
    }

    @Override
    public void deleteExpiredToken(Instant date) {
        refreshTokenRepostiory.deleteExpiredToken(date);
    }
}
