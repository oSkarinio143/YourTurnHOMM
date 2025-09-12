package pl.oskarinio.yourturnhomm.infrastructure.usecase.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.technical.database.H2AdminInitializer;
import pl.oskarinio.yourturnhomm.domain.port.repository.UserRepository;

import java.time.Clock;

@Component
@Profile("h2")
public class H2AdminInitializerUseCase implements CommandLineRunner {
    private final H2AdminInitializer h2AdminInitializer;

    public H2AdminInitializerUseCase(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     Clock clock,
                                     @Value("${app.security.admin-username:}") String adminUsername,
                                     @Value("${app.security.admin-password:}") String adminPassword) {
        this.h2AdminInitializer = new H2AdminInitializer(userRepository, passwordEncoder, clock, adminUsername, adminPassword);
    }

    @Override
    public void run(String... args) throws Exception {
        h2AdminInitializer.initializeProfile();
    }
}
