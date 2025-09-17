package pl.oskarinio.yourturnhomm.infrastructure.adapter.out;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;

@Service
public class PasswordEncoderService implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
}
