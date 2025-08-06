package oskarinio143.yourturnhomm.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oskarinio143.yourturnhomm.model.servicedto.UserServiceData;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {
    private final SecretKey secretKey;
    private final Clock clock;
    private Instant now;

    public TokenService(@Value("${jwt.secret.base64}") String secretKeyString, Clock clock){
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyString)
        );
        this.clock = clock;
    }


    public String generateToken(UserServiceData loginServiceData, long seconds) {
        now = Instant.now(clock);
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.plus(seconds, ChronoUnit.SECONDS));
        return Jwts.builder()
                .setSubject(loginServiceData.getUsername())
                .claim("roles", loginServiceData.getRoles())  // możesz dodać inne dane
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        now = Instant.now(clock);
        Date expirationDate = Date.from(now);
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(expirationDate);
    }

    public boolean isTokenExpiredSafe(String token){
        boolean isTokenExpired = true;
        try{
            isTokenExpired = isTokenExpired(token);
        }catch (ExpiredJwtException | IllegalArgumentException ignored){}
        return isTokenExpired;
    }
}

