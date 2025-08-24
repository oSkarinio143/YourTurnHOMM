package pl.oskarinio.yourturnhomm.app.port.in.user;

import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface TokenUseCase {
    String generateToken(UserServiceData loginServiceData, long seconds);
    String extractUsername(String token);
    boolean isTokenExpiredSafe(String token);
}
