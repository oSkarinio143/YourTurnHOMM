package oskarinio143.yourturnhomm.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import oskarinio143.yourturnhomm.model.form.RegisterForm;
import oskarinio143.yourturnhomm.model.constant.Role;
import oskarinio143.yourturnhomm.model.entity.RefreshToken;
import oskarinio143.yourturnhomm.model.entity.User;
import oskarinio143.yourturnhomm.model.servicedto.UserServiceData;
import oskarinio143.yourturnhomm.repository.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    @Value("${token.refresh.seconds}")
    private long TOKEN_REFRESH_SECONDS;

    public RegisterService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, Clock clock) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Transactional
    public UserServiceData registerUser(RegisterForm registerForm){
        UserServiceData userServiceData = getUserServiceData(registerForm);
        userService.generateAndSetTokens(userServiceData);
        saveData(userServiceData);
        return userServiceData;
    }

    public UserServiceData getUserServiceData(RegisterForm registerForm){
        String hashedPassword = passwordEncoder.encode(registerForm.getPassword());
        UserServiceData userServiceData = new UserServiceData(registerForm.getUsername(), hashedPassword);
        userServiceData.addRole(Role.ROLE_USER);
        return userServiceData;
    }

    @Transactional
    public void saveData(UserServiceData userServiceData){
        Instant now = Instant.now(clock);
        RefreshToken refreshToken = new RefreshToken(userServiceData.getRefreshToken(), now, now.plus(TOKEN_REFRESH_SECONDS, ChronoUnit.SECONDS));
        User user = new User(userServiceData.getUsername(), userServiceData.getPassword(), now);
        user.setRoles(userServiceData.getRoles());
        userService.setRefreshToken(user, refreshToken);
        userRepository.save(user);
    }
}
