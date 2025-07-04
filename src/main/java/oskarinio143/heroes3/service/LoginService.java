package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import oskarinio143.heroes3.exception.UsernameNotMatchingPassword;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.Role;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

@Service
public class LoginService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserServiceData loginUser(LoginForm loginForm){
        User user = getUser(loginForm);
        UserServiceData userServiceData = getUserServiceData(user);
        userService.generateAndSetTokens(userServiceData);
        user.setRefreshToken(userService.getAndSaveRefreshToken(userServiceData.getRefreshToken()));
        return userServiceData;
    }

    public User getUser(LoginForm loginForm){
        User user = userService.getUserByUsernameOrThrow(loginForm.getUsername());
        checkUsernameMatchingPassword(user, loginForm);
        return user;
    }

    public UserServiceData getUserServiceData(User user){
        UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
        userServiceData.setRoles(user.getRoles());
        return userServiceData;
    }

    public void checkUsernameMatchingPassword(User user, LoginForm loginForm){
        if(!isPasswordMatching(loginForm.getPassword(), user.getPassword()))
            throw new UsernameNotMatchingPassword();
    }

    public boolean isPasswordMatching(String loginPassword, String databasePassword){
        return passwordEncoder.matches(loginPassword, databasePassword);
    }
}
