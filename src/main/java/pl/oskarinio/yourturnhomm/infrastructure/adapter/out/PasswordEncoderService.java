package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;

@Slf4j
@Service
public class PasswordEncoderService implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        log.debug("Koduje haslo uzytkownika");
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        log.debug("Sprawdzam zgodnosc hasla. Wynik = {}", matches);
        return matches;
    }
}
