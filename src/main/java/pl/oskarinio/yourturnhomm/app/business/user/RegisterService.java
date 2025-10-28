package pl.oskarinio.yourturnhomm.app.business.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.Register;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.user.RegisterUseCase;

import java.time.Clock;

@Service
@Slf4j
public class RegisterService implements Register {
    private final RegisterUseCase registerUseCase;

    public RegisterService(UserRepository userRepository,
                           UserManagement userManagement,
                           PasswordEncoderPort passwordEncoderPort,
                           Clock clock,
                           @Value("${token.refresh.seconds}") long refreshSeconds) {
        this.registerUseCase = new RegisterUseCase(userRepository, userManagement, passwordEncoderPort, clock, refreshSeconds);
    }

    @Override
    @Transactional
    public UserServiceData registerUser(RegisterForm registerForm) {
        log.debug("Rejestruję użytkownika. Nazwa = {}", registerForm.getUsername());
        return registerUseCase.registerUser(registerForm);
    }
}
