package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenUseCaseTest {
    private static final String secretKeyString = "uC5E8h1xZJt9mEcnzA4pQ3YwZsRbT7dKqLgNjVfH2uX0cB9rOiWsEtG6yPaM5nDd";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString));
    private static final Instant INSTANT = Instant.now();
    private static final Clock clock = Clock.fixed(INSTANT, ZoneOffset.UTC);
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";

    private String token;

    private TokenUseCase tokenUseCase;

    @BeforeEach
    void setUp(){
        tokenUseCase = new TokenUseCase(secretKeyString, clock);

        UserServiceData userServiceData = getUserServiceData();
        token = tokenUseCase.generateToken(userServiceData, 3600);
    }

    @Test
    @DisplayName("Poprawny userServiceData, zwraca poprawny token")
    void generateToken_correctValues_resultCorrectToken(){
        Claims claims = getClaims(token);
        assertThat(claims.getSubject()).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Poprawny userServiceData, inny secretKey, rzuca SignatureException")
    void generateToken_differentSecretKey_resultSignatureException(){
        String differentSecretKeyString = "hX3tKJ4wRz2fL8pVbGqYxM1nW7eD9sA0oT6uN5cQvZrjE4yHkP2mU3iO0lB8dS9X";
        tokenUseCase = new TokenUseCase(differentSecretKeyString, clock);

        UserServiceData userServiceData = getUserServiceData();
        token = tokenUseCase.generateToken(userServiceData, 3600);

        assertThrows(SignatureException.class, () -> getClaims(token));
    }

    @Test
    @DisplayName("Null userServiceData, rzuca NullPointerException")
    void generateToken_nullValues_resultNullPointerException(){
        assertThrows(NullPointerException.class, () -> tokenUseCase.generateToken(null,120));
    }

    @Test
    @DisplayName("Przeszła data ważności, zwraca wygaśnięty token, rzuca ExpiredJwtException")
    void generateToken_negativeTime_resultExpiredToken(){
        UserServiceData userServiceData = getUserServiceData();
        token = tokenUseCase.generateToken(userServiceData,-1);

        assertThrows(ExpiredJwtException.class, () -> getClaims(token));
    }

    @Test
    @DisplayName("Poprawny token, zwraca poprawną nazwę użytkownika")
    void extractUsername_correctToken_resultCorrectUsername(){
        String username = tokenUseCase.extractUsername(token);

        assertThat(username).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Niepoprawny token, rzuca JwtException")
    void extractUsername_incorrectToken_resultJwtException(){
        assertThrows(JwtException.class, () -> tokenUseCase.extractUsername("incorrectToken"));
    }

    @Test
    @DisplayName("Poprawny token, zwraca false")
    void isTokenExpired_correctToken_resultFalse(){
        boolean isExpired = tokenUseCase.isTokenExpiredSafe(token);

        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("Wygaśnięty token, zwraca true")
    void isTokenExpired_expiredToken_resultTrue(){
        UserServiceData userServiceData = getUserServiceData();
        token = tokenUseCase.generateToken(userServiceData,-1);

        boolean isExpired = tokenUseCase.isTokenExpiredSafe(token);
        assertThat(isExpired).isTrue();
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private UserServiceData getUserServiceData(){
        UserServiceData userServiceData = new UserServiceData(USERNAME, PASSWORD);
        userServiceData.setRoles(Set.of(Role.ROLE_USER));
        return userServiceData;
    }
}
