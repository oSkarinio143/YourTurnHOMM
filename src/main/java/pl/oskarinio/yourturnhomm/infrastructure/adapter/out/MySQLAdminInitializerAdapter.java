package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.oskarinio.yourturnhomm.app.port.out.repository.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.rest.MySQLAdminInitializer;

import java.time.Clock;

@Component
@Profile("!h2")
class MySQLAdminInitializerAdapter implements CommandLineRunner {
    private final MySQLAdminInitializer mySQLAdminInitializer;

    public MySQLAdminInitializerAdapter(UserRepositoryPort userRepositoryPort,
                                        PasswordEncoder passwordEncoder,
                                        Clock clock,
                                        @Value("${app.security.admin-username:}") String adminUsername,
                                        @Value("${app.security.admin-password:}") String adminPassword) {
        this.mySQLAdminInitializer = new MySQLAdminInitializer(userRepositoryPort, passwordEncoder, clock, adminUsername, adminPassword);
    }

    @Override
    public void run(String... args) throws Exception {
        mySQLAdminInitializer.initializeProfile();
    }
}
