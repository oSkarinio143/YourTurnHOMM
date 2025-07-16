package oskarinio143.heroes3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.constant.Role;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.repository.UserRepository;

import java.time.Clock;
import java.time.Instant;

@Component
public class FirstAdminConfig implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    public FirstAdminConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, Clock clock) {
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
