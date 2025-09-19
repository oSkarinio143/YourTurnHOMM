package pl.oskarinio.yourturnhomm.app.business.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.Token;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.user.UserUseCase;

import java.time.Clock;
import java.util.List;

@Service
@Slf4j
public class UserServiceAdapter implements UserManagement {
    private final UserUseCase userUseCase;

    public UserServiceAdapter(UserRepository userRepository,
                              Token token,
                              Clock clock,
                              @Value("${token.access.seconds}") long accessSeconds,
                              @Value("${token.refresh.seconds}") long tokenSeconds){
        this.userUseCase = new UserUseCase(userRepository, token, clock, accessSeconds, tokenSeconds);
    }

    @Override
    public void generateAndSetTokens(UserServiceData userServiceData) {
        log.debug("Generuję i ustawiam tokeny dla użytkownika. Nazwa = {}", userServiceData.getUsername());
        userUseCase.generateAndSetTokens(userServiceData);
    }

    @Override
    public RefreshToken getRefreshToken(String refreshTokenString) {
        log.debug("Pobieram refresh token");
        return userUseCase.getRefreshToken(refreshTokenString);
    }

    @Override
    public String prepareErrorMessage(List<String> errorsMessageList) {
        log.debug("Przygotowuję wiadomość błędu dla użytkownika");
        return userUseCase.prepareErrorMessage(errorsMessageList);
    }

    @Override
    public User getUserByUsernameOrThrow(String username) {
        log.trace("Pobieram użytkownika z bazy. Nazwa = {}", username);
        return userUseCase.getUserByUsernameOrThrow(username);
    }

    @Transactional
    @Override
    public void deleteToken(String username) {
        log.debug("Usuwam token użytkownika. Nazwa = {}", username);
        userUseCase.deleteToken(username);
    }

    @Transactional
    @Override
    public void setRefreshToken(User user, RefreshToken refreshToken) {
        log.debug("Ustawiam refresh token dla użytkownika. Nazwa = {}", user.getUsername());
        userUseCase.setRefreshToken(user, refreshToken);
    }
}
