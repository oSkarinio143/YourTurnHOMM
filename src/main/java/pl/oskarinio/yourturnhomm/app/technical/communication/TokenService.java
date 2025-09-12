package pl.oskarinio.yourturnhomm.app.technical.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Token;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.TokenUseCase;

import java.time.Clock;

@Service
public class TokenService implements Token {
    private final TokenUseCase tokenUseCase;

    public TokenService(@Value("${jwt.secret.base64}") String secretKeyString, Clock clock) {
        this.tokenUseCase = new TokenUseCase(secretKeyString, clock);
    }

    @Override
    public String generateToken(UserServiceData loginServiceData, long seconds) {
        return tokenUseCase.generateToken(loginServiceData,seconds);
    }

    @Override
    public String extractUsername(String token) {
        return tokenUseCase.extractUsername(token);
    }

    @Override
    public boolean isTokenExpiredSafe(String token) {
        return tokenUseCase.isTokenExpiredSafe(token);
    }
}
