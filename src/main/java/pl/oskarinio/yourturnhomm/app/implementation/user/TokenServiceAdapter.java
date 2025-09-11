package pl.oskarinio.yourturnhomm.app.implementation.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.infrastructure.temp.TokenService;
import pl.oskarinio.yourturnhomm.infrastructure.temp.TokenUseCase;

import java.time.Clock;

@Service
public class TokenServiceAdapter implements TokenUseCase {
    private final TokenService tokenService;

    public TokenServiceAdapter(@Value("${jwt.secret.base64}") String secretKeyString, Clock clock) {
        this.tokenService = new TokenService(secretKeyString, clock);
    }

    @Override
    public String generateToken(UserServiceData loginServiceData, long seconds) {
        return tokenService.generateToken(loginServiceData,seconds);
    }

    @Override
    public String extractUsername(String token) {
        return tokenService.extractUsername(token);
    }

    @Override
    public boolean isTokenExpiredSafe(String token) {
        return tokenService.isTokenExpiredSafe(token);
    }
}
