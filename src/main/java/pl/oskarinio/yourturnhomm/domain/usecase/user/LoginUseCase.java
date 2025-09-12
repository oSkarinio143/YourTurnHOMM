package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotMatchingPassword;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.user.User;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;
import pl.oskarinio.yourturnhomm.infrastructure.temp.RefreshToken;

public class LoginService {

    private final User user;
    private final PasswordEncoder passwordEncoder;

    public LoginService(User user, PasswordEncoder passwordEncoder) {
        this.user = user;
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceData loginUser(LoginForm loginForm){
        pl.oskarinio.yourturnhomm.domain.model.user.User user = getUser(loginForm);
        UserServiceData userServiceData = getUserServiceData(user);
        this.user.generateAndSetTokens(userServiceData);
        RefreshToken refreshToken = this.user.getRefreshToken(userServiceData.getRefreshToken());
        this.user.setRefreshToken(user, refreshToken);
        return userServiceData;
    }

    private pl.oskarinio.yourturnhomm.domain.model.user.User getUser(LoginForm loginForm){
        pl.oskarinio.yourturnhomm.domain.model.user.User user = this.user.getUserByUsernameOrThrow(loginForm.getUsername());
        checkUsernameMatchingPassword(user, loginForm);
        return user;
    }

    private UserServiceData getUserServiceData(pl.oskarinio.yourturnhomm.domain.model.user.User user){
        UserServiceData userServiceData = new UserServiceData(user.getUsername(), user.getPassword());
        userServiceData.setRoles(user.getRoles());
        return userServiceData;
    }

    private void checkUsernameMatchingPassword(pl.oskarinio.yourturnhomm.domain.model.user.User user, LoginForm loginForm){
        if(!isPasswordMatching(loginForm.getPassword(), user.getPassword()))
            throw new UsernameNotMatchingPassword();
    }

    private boolean isPasswordMatching(String loginPassword, String databasePassword){
        return passwordEncoder.matches(loginPassword, databasePassword);
    }
}
