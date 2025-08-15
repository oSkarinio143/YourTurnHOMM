package pl.oskarinio.yourturnhomm.app.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.domain.rest.service.MySQLAdminInitializer;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.time.Clock;

@Component
@Profile("!h2")
public class MySQLAdminInitializerAdapter implements CommandLineRunner {
    private final MySQLAdminInitializer mySQLAdminInitializer;

    public MySQLAdminInitializerAdapter(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder,
                                        Clock clock,
                                        @Value("${app.security.admin-username:}") String adminUsername,
                                        @Value("${app.security.admin-password:}") String adminPassword) {
        this.mySQLAdminInitializer = new MySQLAdminInitializer(userRepository, passwordEncoder, clock, adminUsername, adminPassword);
    }

    @Override
    public void run(String... args) throws Exception {
        mySQLAdminInitializer.initializeProfile();
    }
}
