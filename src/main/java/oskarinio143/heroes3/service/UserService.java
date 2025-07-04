package oskarinio143.heroes3.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import oskarinio143.heroes3.exception.UsernameNotFoundException;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(UserRepository userRepository, TokenService tokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void generateAndSetTokens(UserServiceData userServiceData){
        userServiceData.setAccessToken(tokenService.generateToken(userServiceData, 3600000));
        userServiceData.setRefreshToken(tokenService.generateToken(userServiceData, 3600000 * 24 * 7));
    }

    public RefreshToken getAndSaveRefreshToken(String refreshTokenString){
        RefreshToken refreshToken = new RefreshToken(refreshTokenString);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;

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
}
