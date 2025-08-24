package pl.oskarinio.yourturnhomm.app.implementation.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.app.port.in.user.TokenUseCase;
import pl.oskarinio.yourturnhomm.app.port.in.user.UserUseCase;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.service.user.UserService;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;

import java.time.Clock;
import java.util.List;

@Service
public class UserServiceAdapter implements UserUseCase {
    private final UserService userService;

    public UserServiceAdapter(UserRepositoryPort userRepositoryPort,
                              TokenUseCase tokenUseCase,
                              Clock clock,
                              @Value("${token.access.seconds}") long accessSeconds,
                              @Value("${token.refresh.seconds}") long tokenSeconds){
        this.userService = new UserService(userRepositoryPort, tokenUseCase, clock, accessSeconds, tokenSeconds);
    }

    @Override
    public void generateAndSetTokens(UserServiceData userServiceData) {
        userService.generateAndSetTokens(userServiceData);
    }

    @Override
    public RefreshToken getRefreshToken(String refreshTokenString) {
        return userService.getRefreshToken(refreshTokenString);
    }

    @Override
    public String prepareErrorMessage(List<ObjectError> errorsMessageList) {
        return userService.prepareErrorMessage(errorsMessageList);
    }

    @Override
    public User getUserByUsernameOrThrow(String username) {
        return userService.getUserByUsernameOrThrow(username);
    }

    @Transactional
    @Override
    public void deleteToken(String username) {
        userService.deleteToken(username);
    }

    @Transactional
    @Override
    public void setRefreshToken(User user, RefreshToken refreshToken) {
        userService.setRefreshToken(user,refreshToken);
    }
}
