package oskarinio143.heroes3.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.Role;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserServiceData getUserServiceData(LoginForm loginForm){
        String hashedPassword = passwordEncoder.encode(loginForm.getPassword());
        UserServiceData userServiceData = new UserServiceData(loginForm.getUsername(), hashedPassword);
        userServiceData.addRole(Role.ROLE_USER.name());
        return userServiceData;
    }

    public void generateAndSetTokens(UserServiceData loginServiceData){
        loginServiceData.setAccessToken(tokenService.generateToken(loginServiceData, 3600000));
        loginServiceData.setRefreshToken(tokenService.generateToken(loginServiceData, 3600000 * 24 * 7));
    }
}
