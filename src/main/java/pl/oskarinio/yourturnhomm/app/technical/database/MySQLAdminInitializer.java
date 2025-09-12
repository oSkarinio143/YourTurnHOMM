package pl.oskarinio.yourturnhomm.infrastructure.temp;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.repository.UserRepositoryPort;

import java.time.Clock;
import java.time.Instant;

public class MySQLAdminInitializer{

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private String adminUsername;
    private String adminPassword;

    public MySQLAdminInitializer(UserRepositoryPort userRepositoryPort,
                                 PasswordEncoder passwordEncoder,
                                 Clock clock,
                                 String adminUsername,
                                 String adminPassword) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public void initializeProfile() {
        if (adminUsername == null || adminUsername.isBlank() || adminPassword == null || adminPassword.isBlank())
            return;
        if (!userRepositoryPort.findByUsername(adminUsername).isPresent()) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRegistrationDate(Instant.now(clock));
            adminUser.addRole(Role.ROLE_USER);
            adminUser.addRole(Role.ROLE_ADMIN);
            userRepositoryPort.save(adminUser);
        }
    }
}
