package pl.oskarinio.yourturnhomm.domain.port.out;

import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface Token {
    String generateToken(UserServiceData loginServiceData, long seconds);
    String extractUsername(String token);
    boolean isTokenExpiredSafe(String token);
}
