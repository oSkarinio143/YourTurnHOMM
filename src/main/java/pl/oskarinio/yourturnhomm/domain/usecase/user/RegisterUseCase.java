package pl.oskarinio.yourturnhomm.domain.usecase.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.User;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;
import pl.oskarinio.yourturnhomm.infrastructure.temp.RefreshToken;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RegisterService {
    private final UserRepository userRepository;
    private final User user;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private long TOKEN_REFRESH_SECONDS;

    public RegisterService(UserRepository userRepository, User user, PasswordEncoder passwordEncoder, Clock clock, long refreshSeconds) {
        this.userRepository = userRepository;
        this.user = user;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        TOKEN_REFRESH_SECONDS = refreshSeconds;
    }

    public UserServiceData registerUser(RegisterForm registerForm){
        UserServiceData userServiceData = getUserServiceData(registerForm);
        user.generateAndSetTokens(userServiceData);
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
        pl.oskarinio.yourturnhomm.domain.model.user.User user = new pl.oskarinio.yourturnhomm.domain.model.user.User(userServiceData.getUsername(), userServiceData.getPassword(), now);
        user.setRoles(userServiceData.getRoles());
        this.user.setRefreshToken(user, refreshToken);
        userRepository.save(user);
    }
}
