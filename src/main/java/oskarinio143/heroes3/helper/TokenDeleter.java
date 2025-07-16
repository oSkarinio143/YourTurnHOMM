package oskarinio143.heroes3.helper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;
import oskarinio143.heroes3.service.TokenService;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;

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

    //Usuwa wygasłe RefreshTokeny z bazy danych co 24 godziny
    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanExpiredTokens(){
        Instant now = Instant.now(clock);
        userRepository.removeRefreshTokenRelation(now);
        refreshTokenRepository.deleteExpiredToken(now);
    }
}
