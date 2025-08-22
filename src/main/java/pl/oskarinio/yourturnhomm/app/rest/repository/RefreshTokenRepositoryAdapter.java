package pl.oskarinio.yourturnhomm.app.rest.repository;

import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.rest.port.out.RefreshTokenRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.RefreshTokenRepository;

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
