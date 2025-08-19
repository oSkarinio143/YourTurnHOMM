package pl.oskarinio.yourturnhomm.app.user.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.app.user.port.in.RegisterUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.service.user.RegisterService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;

import java.time.Clock;

@Service
public class RegisterServiceAdapter implements RegisterUseCase {
    private final RegisterService registerService;

    public RegisterServiceAdapter(UserRepositoryPort userRepository,
                                  UserUseCase userUseCase,
                                  PasswordEncoder passwordEncoder,
                                  Clock clock,
                                  @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.registerService = new RegisterService(userRepository, userUseCase, passwordEncoder, clock, refreshSeconds);
    }

    @Transactional
    @Override
    public UserServiceData registerUser(RegisterForm registerForm) {
        return registerService.registerUser(registerForm);
    }
}
