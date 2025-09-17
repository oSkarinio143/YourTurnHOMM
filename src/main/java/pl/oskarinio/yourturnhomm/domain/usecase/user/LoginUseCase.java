package pl.oskarinio.yourturnhomm.domain.usecase.user;

import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotMatchingPassword;
import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;

public class LoginUseCase {

    private final UserManagement userManagement;
    private final PasswordEncoderPort passwordEncoderPort;

    public LoginUseCase(UserManagement userManagement, PasswordEncoderPort passwordEncoderPort) {
        this.userManagement = userManagement;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    public UserServiceData loginUser(LoginForm loginForm){
        User user = getUser(loginForm);
        UserServiceData userServiceData = getUserServiceData(user);
        this.userManagement.generateAndSetTokens(userServiceData);
        RefreshToken refreshToken = this.userManagement.getRefreshToken(userServiceData.getRefreshToken());
        this.userManagement.setRefreshToken(user, refreshToken);
        return userServiceData;
    }

    private User getUser(LoginForm loginForm){
        User user = this.userManagement.getUserByUsernameOrThrow(loginForm.getUsername());
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
        return passwordEncoderPort.matches(loginPassword, databasePassword);
    }
}
