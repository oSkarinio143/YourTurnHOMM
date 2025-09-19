package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.infrastructure.usecase.communication.TokenUseCase;

import java.time.Clock;

@Slf4j
@Service
public class TokenService implements Token {
    private final TokenUseCase tokenUseCase;

    public TokenService(@Value("${jwt.secret.base64}") String secretKeyString, Clock clock) {
        this.tokenUseCase = new TokenUseCase(secretKeyString, clock);
    }

    @Override
    public String generateToken(UserServiceData loginServiceData, long seconds) {
        log.debug("Generuje token dla uzytkownika. Nazwa = {}", loginServiceData.getUsername());
        return tokenUseCase.generateToken(loginServiceData,seconds);
    }

    @Override
    public String extractUsername(String token) {
        String username = tokenUseCase.extractUsername(token);
        log.trace("Wyciagam nazwe uzytkownika z tokenu. Nazwa = {}", username);
        return username;
    }

    @Override
    public boolean isTokenExpiredSafe(String token) {
        boolean expired = tokenUseCase.isTokenExpiredSafe(token);
        log.trace("Sprawdzam czy token wygasl. Wynik = {}", expired);
        return expired;
    }
}
