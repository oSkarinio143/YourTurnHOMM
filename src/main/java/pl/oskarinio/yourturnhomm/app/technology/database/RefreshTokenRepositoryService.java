package pl.oskarinio.yourturnhomm.app.technology.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.database.repository.RefreshTokenRepositoryUseCase;

import java.time.Instant;

@Slf4j
@Component
public class RefreshTokenRepositoryService implements pl.oskarinio.yourturnhomm.infrastructure.port.database.RefreshTokenRepository {
    private final RefreshTokenRepositoryUseCase refreshTokenRepostiory;

    public RefreshTokenRepositoryService(RefreshTokenRepositoryUseCase jpaRepository) {
        this.refreshTokenRepostiory = jpaRepository;
    }

    @Override
    public void deleteExpiredToken(Instant date) {
        log.debug("Usuwam wygasłe tokeny");
        refreshTokenRepostiory.deleteExpiredToken(date);
    }
}
