package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RegisterUseCase {
    private final UserRepository userRepository;
    private final UserManagement userManagement;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private long TOKEN_REFRESH_SECONDS;

    public RegisterUseCase(UserRepository userRepository, UserManagement userManagement, PasswordEncoder passwordEncoder, Clock clock, long refreshSeconds) {
        this.userRepository = userRepository;
        this.userManagement = userManagement;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        TOKEN_REFRESH_SECONDS = refreshSeconds;
    }

    public UserServiceData registerUser(RegisterForm registerForm){
        UserServiceData userServiceData = getUserServiceData(registerForm);
        userManagement.generateAndSetTokens(userServiceData);
        saveData(userServiceData);
        return userServiceData;
    }

    private UserServiceData getUserServiceData(RegisterForm registerForm){
        String hashedPassword = passwordEncoder.encode(registerForm.getPassword());
        UserServiceData userServiceData = new UserServiceData(registerForm.getUsername(), hashedPassword);
        userServiceData.addRole(Role.ROLE_USER);
        return userServiceData;
    }

    private void saveData(UserServiceData userServiceData){
        Instant now = Instant.now(clock);
        RefreshToken refreshToken = new RefreshToken(userServiceData.getRefreshToken(), now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
        User user = new User(userServiceData.getUsername(), userServiceData.getPassword(), now);
        user.setRoles(userServiceData.getRoles());
        this.userManagement.setRefreshToken(user, refreshToken);
        userRepository.save(user);
    }
}
