package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.port.out.repository.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.infrastructure.temp.H2AdminInitializer;

import java.time.Clock;

@Component
@Profile("h2")
public class H2AdminInitializerAdapter implements CommandLineRunner {
    private final H2AdminInitializer h2AdminInitializer;

    public H2AdminInitializerAdapter(UserRepositoryPort userRepositoryPort,
                                     PasswordEncoder passwordEncoder,
                                     Clock clock,
                                     @Value("${app.security.admin-username:}") String adminUsername,
                                     @Value("${app.security.admin-password:}") String adminPassword) {
        this.h2AdminInitializer = new H2AdminInitializer(userRepositoryPort, passwordEncoder, clock, adminUsername, adminPassword);
    }

    @Override
    public void run(String... args) throws Exception {
        h2AdminInitializer.initializeProfile();
    }
}
