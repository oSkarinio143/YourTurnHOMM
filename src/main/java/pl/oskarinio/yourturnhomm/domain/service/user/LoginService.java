package pl.oskarinio.yourturnhomm.domain.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.app.port.in.user.UserUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.UsernameNotMatchingPassword;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public class LoginService {

    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserUseCase userUseCase, PasswordEncoder passwordEncoder) {
        this.userUseCase = userUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceData loginUser(LoginForm loginForm){
        User user = getUser(loginForm);
        UserServiceData userServiceData = getUserServiceData(user);
        userUseCase.generateAndSetTokens(userServiceData);
        RefreshToken refreshToken = userUseCase.getRefreshToken(userServiceData.getRefreshToken());
        userUseCase.setRefreshToken(user, refreshToken);
        return userServiceData;
    }

    private User getUser(LoginForm loginForm){
        User user = userUseCase.getUserByUsernameOrThrow(loginForm.getUsername());
        checkUsernameMatchingPassword(user, loginForm);
        return user;
    }

    private UserServiceData getUserServiceData(User user){
        UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
        userServiceData.setRoles(user.getRoles());
        return userServiceData;
    }

    private void checkUsernameMatchingPassword(User user, LoginForm loginForm){
        if(!isPasswordMatching(loginForm.getPassword(), user.getPassword()))
            throw new UsernameNotMatchingPassword();
    }

    private boolean isPasswordMatching(String loginPassword, String databasePassword){
        return passwordEncoder.matches(loginPassword, databasePassword);
    }
}
