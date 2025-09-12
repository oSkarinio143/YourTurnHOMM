package pl.oskarinio.yourturnhomm.app.implementation.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.user.Login;
import pl.oskarinio.yourturnhomm.domain.port.user.User;
import pl.oskarinio.yourturnhomm.domain.usecase.user.LoginUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;

@Service
public class LoginService implements Login {
    private final LoginUseCase loginUseCase;

    public LoginService(User user, PasswordEncoder passwordEncoder) {
        this.loginUseCase = new LoginUseCase(user, passwordEncoder);
    }

    @Transactional
    @Override
    public UserServiceData loginUser(LoginForm loginForm) {
        return loginUseCase.loginUser(loginForm);
    }
}
