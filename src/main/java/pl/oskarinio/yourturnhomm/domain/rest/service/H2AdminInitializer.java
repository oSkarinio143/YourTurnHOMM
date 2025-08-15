package pl.oskarinio.yourturnhomm.domain.rest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.oskarinio.yourturnhomm.domain.user.model.Role;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.time.Instant;
import java.util.Set;

public class H2AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private String adminUsername;
    private String adminPassword;

    public H2AdminInitializer(UserRepository userRepository,
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
