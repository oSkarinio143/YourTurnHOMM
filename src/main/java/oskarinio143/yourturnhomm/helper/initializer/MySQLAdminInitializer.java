package oskarinio143.yourturnhomm.helper.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import oskarinio143.yourturnhomm.model.constant.Role;
import oskarinio143.yourturnhomm.model.entity.User;
import oskarinio143.yourturnhomm.repository.UserRepository;

import java.time.Clock;
import java.time.Instant;

@Component
@Profile("!h2")
public class MySQLAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Value("${app.security.admin-username:}")
    private String adminUsername;

    @Value("${app.security.admin-password:}")
    private String adminPassword;

    public MySQLAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminUsername == null || adminUsername.isBlank() || adminPassword == null || adminPassword.isBlank())
            return;

        if (!userRepository.findByUsername(adminUsername).isPresent()) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRegistrationDate(Instant.now(clock));
            adminUser.addRole(Role.ROLE_USER);
            adminUser.addRole(Role.ROLE_ADMIN);
            userRepository.save(adminUser);
        }
    }
}
