package pl.oskarinio.yourturnhomm.domain.user.port.in;

import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;

public interface TokenUseCase {
    String generateToken(UserServiceData loginServiceData, long seconds);
    String extractUsername(String token);
    boolean isTokenExpiredSafe(String token);
}
