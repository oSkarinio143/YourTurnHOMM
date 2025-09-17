package pl.oskarinio.yourturnhomm.app.technology.database;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;

import java.time.Clock;
import java.time.Instant;

public class H2AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private String adminUsername;
    private String adminPassword;

    public H2AdminInitializer(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              Clock clock,
                              String adminUsername,
                              String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public void initializeProfile() {
        if (userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRegistrationDate(Instant.now());
            adminUser.addRole(Role.ROLE_USER);
            adminUser.addRole(Role.ROLE_ADMIN);
            userRepository.save(adminUser);
        }
    }
}
