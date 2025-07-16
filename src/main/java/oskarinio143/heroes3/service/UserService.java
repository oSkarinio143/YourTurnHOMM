package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import oskarinio143.heroes3.exception.UsernameNotFoundException;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final Clock clock;

    public UserService(UserRepository userRepository, TokenService tokenService, Clock clock) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.clock = clock;
    }

    public void generateAndSetTokens(UserServiceData userServiceData){
        userServiceData.setAccessToken(tokenService.generateToken(userServiceData, 3600000 / 4));
        userServiceData.setRefreshToken(tokenService.generateToken(userServiceData, 3600000 * 24 * 7));
    }

    public RefreshToken getRefreshToken(String refreshTokenString){
        Instant now = Instant.now(clock);
        return new RefreshToken(refreshTokenString, now, now.plus(7, ChronoUnit.DAYS));

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

    @Transactional
    public void deleteToken(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        setRefreshToken(user, null);
    }

    @Transactional
    public void setRefreshToken(User user, RefreshToken refreshToken){
        if(refreshToken == null && user.getRefreshToken() != null)
            user.setRefreshToken(null);
        else if (refreshToken.getUser() != user)
            user.setRefreshToken(refreshToken);
    }
}
