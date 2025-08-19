package pl.oskarinio.yourturnhomm.domain.service.rest;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;

import java.time.Instant;
import java.util.Set;

public class H2AdminInitializer {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private String adminUsername;
    private String adminPassword;

    public H2AdminInitializer(UserRepositoryPort userRepository,
                              PasswordEncoder passwordEncoder,
                              String adminUsername,
                              String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public void initializeProfile() {
        if (userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
            adminUser.setRegistrationDate(Instant.now());
            userRepository.save(adminUser);
        }
    }
}
