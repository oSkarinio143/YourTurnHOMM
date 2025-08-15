package pl.oskarinio.yourturnhomm.app.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.user.port.in.RegisterUseCase;
import pl.oskarinio.yourturnhomm.domain.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.user.service.RegisterService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.time.Clock;

@Service
public class RegisterServiceAdapter implements RegisterUseCase {
    private final RegisterService registerService;

    public RegisterServiceAdapter(UserRepository userRepository,
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
