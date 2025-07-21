package oskarinio143.heroes3.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import oskarinio143.heroes3.model.constant.Role;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.repository.UserRepository;

import java.time.Instant;
import java.util.Set;

@Component
@Profile("h2")
public class H2AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String adminUsername = "adminUser";
    private String adminPassword = "1234";

    public H2AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
            adminUser.setRegistrationDate(Instant.now());
            userRepository.save(adminUser);
        }
    }
}
