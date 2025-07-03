package oskarinio143.heroes3.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.servicedto.UserServiceData;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {
    private final SecretKey secretKey;

    public TokenService(@Value("${jwt.secret.base64}") String secretKeyString){
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyString)
        );
    }

    public String generateToken(UserServiceData loginServiceData, long time) {
        return Jwts.builder()
                .setSubject(loginServiceData.getUsername())
                .claim("roles", loginServiceData.getRoles())  // możesz dodać inne dane
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
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
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}

