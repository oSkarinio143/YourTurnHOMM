package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.springframework.validation.ObjectError;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.UserRepository;
import pl.oskarinio.yourturnhomm.infrastructure.temp.RefreshToken;
import pl.oskarinio.yourturnhomm.infrastructure.temp.TokenUseCase;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final TokenUseCase tokenUseCase;
    private final Clock clock;
    private long TOKEN_ACCESS_SECONDS;
    private long TOKEN_REFRESH_SECONDS;

    public UserService(UserRepository userRepository, TokenUseCase tokenUseCase, Clock clock, long accessSeconds, long refreshSeconds) {
        this.userRepository = userRepository;
        this.tokenUseCase = tokenUseCase;
        this.clock = clock;
        this.TOKEN_ACCESS_SECONDS = accessSeconds;
        this.TOKEN_REFRESH_SECONDS = refreshSeconds;
    }

    public void generateAndSetTokens(UserServiceData userServiceData){
        userServiceData.setAccessToken(tokenUseCase.generateToken(userServiceData, TOKEN_ACCESS_SECONDS));
        userServiceData.setRefreshToken(tokenUseCase.generateToken(userServiceData, TOKEN_REFRESH_SECONDS));
    }

    public RefreshToken getRefreshToken(String refreshTokenString){
        Instant now = Instant.now(this.clock);
        return new RefreshToken(refreshTokenString, now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
    }

    public String prepareErrorMessage(List<ObjectError> errorsMessageList){
        StringBuilder errorMessage = new StringBuilder();
        errorsMessageList.forEach(v -> errorMessage.append(v.getDefaultMessage() + "<br>"));
        return errorMessage.toString();
    }

    public User getUserByUsernameOrThrow(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() ->new UsernameNotFoundException(username));
    }

    public void deleteToken(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> setRefreshToken(user, null));
    }

    public void setRefreshToken(User user, RefreshToken refreshToken){
        if(refreshToken == null && user.getRefreshToken() != null) {
            user.setRefreshToken(null);
        }
        else if (user.getRefreshToken() != refreshToken) {
            user.setRefreshToken(refreshToken);
        }
    }
}
