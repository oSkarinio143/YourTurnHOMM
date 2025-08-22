package pl.oskarinio.yourturnhomm.domain.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.app.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.RefreshToken;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RegisterService {
    private final UserRepositoryPort userRepositoryPort;
    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private long TOKEN_REFRESH_SECONDS;

    public RegisterService(UserRepositoryPort userRepositoryPort, UserUseCase userUseCase, PasswordEncoder passwordEncoder, Clock clock, long refreshSeconds) {
        this.userRepositoryPort = userRepositoryPort;
        this.userUseCase = userUseCase;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        TOKEN_REFRESH_SECONDS = refreshSeconds;
    }

    public UserServiceData registerUser(RegisterForm registerForm){
        UserServiceData userServiceData = getUserServiceData(registerForm);
        userUseCase.generateAndSetTokens(userServiceData);
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
        userUseCase.setRefreshToken(user, refreshToken);
        userRepositoryPort.save(user);
    }
}
