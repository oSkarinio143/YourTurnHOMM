package pl.oskarinio.yourturnhomm.app.user.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.app.user.port.in.LoginUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.service.user.LoginService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

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
