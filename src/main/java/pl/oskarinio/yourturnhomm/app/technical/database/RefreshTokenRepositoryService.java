package pl.oskarinio.yourturnhomm.app.technical.database;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.RefreshTokenRepositoryUseCase;

import java.time.Instant;

@Component
public class RefreshTokenRepositoryService implements pl.oskarinio.yourturnhomm.infrastructure.port.database.RefreshTokenRepository {
    private final RefreshTokenRepositoryUseCase refreshTokenRepostiory;

    public RefreshTokenRepositoryService(RefreshTokenRepositoryUseCase jpaRepository) {
        this.refreshTokenRepostiory = jpaRepository;
    }

    @Override
    public void deleteExpiredToken(Instant date) {
        refreshTokenRepostiory.deleteExpiredToken(date);
    }
}
