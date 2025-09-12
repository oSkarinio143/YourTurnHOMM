package pl.oskarinio.yourturnhomm.app.implementation.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.User;
import pl.oskarinio.yourturnhomm.domain.usecase.user.UserUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.temp.RefreshToken;
import pl.oskarinio.yourturnhomm.infrastructure.temp.Token;

import java.time.Clock;
import java.util.List;

@Service
public class UserServiceAdapter implements User {
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
        userUseCase.generateAndSetTokens(userServiceData);
    }

    @Override
    public RefreshToken getRefreshToken(String refreshTokenString) {
        return userUseCase.getRefreshToken(refreshTokenString);
    }

    @Override
    public String prepareErrorMessage(List<ObjectError> errorsMessageList) {
        return userUseCase.prepareErrorMessage(errorsMessageList);
    }

    @Override
    public pl.oskarinio.yourturnhomm.domain.model.user.User getUserByUsernameOrThrow(String username) {
        return userUseCase.getUserByUsernameOrThrow(username);
    }

    @Transactional
    @Override
    public void deleteToken(String username) {
        userUseCase.deleteToken(username);
    }

    @Transactional
    @Override
    public void setRefreshToken(pl.oskarinio.yourturnhomm.domain.model.user.User user, RefreshToken refreshToken) {
        userUseCase.setRefreshToken(user,refreshToken);
    }
}
