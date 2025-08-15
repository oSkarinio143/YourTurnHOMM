package pl.oskarinio.yourturnhomm.zzz;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.RefreshTokenRepository;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.time.Clock;
import java.time.Instant;

@Component
public class TokenDeleter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    @Autowired
    public TokenDeleter(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanExpiredTokens(){
        Instant now = Instant.now(clock);
        userRepository.removeRefreshTokenRelation(now);
        refreshTokenRepository.deleteExpiredToken(now);
    }
}
