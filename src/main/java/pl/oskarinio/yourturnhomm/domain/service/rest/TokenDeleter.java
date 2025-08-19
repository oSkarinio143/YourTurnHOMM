package pl.oskarinio.yourturnhomm.domain.service.rest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.user.port.out.RefreshTokenRepositoryPort;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;

import java.time.Clock;
import java.time.Instant;

public class TokenDeleter {

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final UserRepositoryPort userRepository;
    private final Clock clock;

    public TokenDeleter(RefreshTokenRepositoryPort refreshTokenRepositoryPort, UserRepositoryPort userRepository, Clock clock) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public void cleanExpiredTokens(){
        Instant now = Instant.now(clock);
        userRepository.removeRefreshTokenRelation(now);
        refreshTokenRepositoryPort.deleteExpiredToken(now);
    }
}