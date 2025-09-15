package pl.oskarinio.yourturnhomm.infrastructure.usecase.communication;

import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoder;

public class PasswordEncoderUseCase implements PasswordEncoder{
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public PasswordEncoderUseCase(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
}
