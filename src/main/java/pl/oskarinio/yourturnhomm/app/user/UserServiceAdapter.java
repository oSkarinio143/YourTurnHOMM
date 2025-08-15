package pl.oskarinio.yourturnhomm.app.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.user.port.in.TokenUseCase;
import pl.oskarinio.yourturnhomm.domain.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.user.service.UserService;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.model.entity.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.time.Clock;
import java.util.List;

@Service
public class UserServiceAdapter implements UserUseCase {
    private final UserService userService;

    public UserServiceAdapter(UserRepository userRepository,
            TokenUseCase tokenUseCase,
            Clock clock,
            @Value("${token.access.seconds}") long accessSeconds,
            @Value("${token.refresh.seconds}") long tokenSeconds){
        this.userService = new UserService(userRepository, tokenUseCase, clock, accessSeconds, tokenSeconds);
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
