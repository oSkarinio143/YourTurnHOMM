package pl.oskarinio.yourturnhomm.app.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.rest.service.H2AdminInitializer;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

@Component
@Profile("h2")
public class H2AdminInitializerAdapter implements CommandLineRunner {
    private final H2AdminInitializer h2AdminInitializer;

    public H2AdminInitializerAdapter(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     @Value("${app.security.admin-username:}") String adminUsername,
                                     @Value("${app.security.admin-password:}") String adminPassword) {
        this.h2AdminInitializer = new H2AdminInitializer(userRepository, passwordEncoder, adminUsername, adminPassword);
    }

    @Override
    public void run(String... args) throws Exception {
        h2AdminInitializer.initializeProfile();
    }
}
