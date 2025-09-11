package pl.oskarinio.yourturnhomm.app.implementation.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.in.user.LoginUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.user.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.service.user.LoginService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;

@Service
public class LoginServiceAdapter implements LoginUseCase {
    private final LoginService loginService;

    public LoginServiceAdapter(UserUseCase userUseCase, PasswordEncoder passwordEncoder) {
        this.loginService = new LoginService(userUseCase, passwordEncoder);
    }

    @Transactional
    @Override
    public UserServiceData loginUser(LoginForm loginForm) {
        return loginService.loginUser(loginForm);
    }
}
