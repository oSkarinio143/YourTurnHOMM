package pl.oskarinio.yourturnhomm.app.implementation.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.Register;
import pl.oskarinio.yourturnhomm.domain.port.user.User;
import pl.oskarinio.yourturnhomm.domain.usecase.user.RegisterUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;

import java.time.Clock;

@Service
public class RegisterService implements Register {
    private final RegisterUseCase registerUseCase;

    public RegisterService(UserRepository userRepository,
                           User user,
                           PasswordEncoder passwordEncoder,
                           Clock clock,
                           @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.registerUseCase = new RegisterUseCase(userRepository, user, passwordEncoder, clock, refreshSeconds);
    }

    @Transactional
    @Override
    public UserServiceData registerUser(RegisterForm registerForm) {
        return registerUseCase.registerUser(registerForm);
    }
}
