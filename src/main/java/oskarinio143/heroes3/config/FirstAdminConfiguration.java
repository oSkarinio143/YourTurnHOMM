package oskarinio143.heroes3.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.Role;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.repository.UserRepository;

@Component
public class FirstAdminConfiguration implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    public FirstAdminConfiguration(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminUsername == null || adminUsername.isBlank() || adminPassword == null || adminPassword.isBlank())
            return;

        // Sprawdzamy, czy użytkownik admin już istnieje w bazie danych
        if (!userRepository.findByUsername(adminUsername).isPresent()) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            // Haszujemy hasło przed zapisaniem!
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.addRole(Role.ROLE_USER.name());
            adminUser.addRole(Role.ROLE_ADMIN.name());

            userRepository.save(adminUser);
        }
    }
}
