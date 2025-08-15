package pl.oskarinio.yourturnhomm.app.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.user.port.in.LoginUseCase;
import pl.oskarinio.yourturnhomm.domain.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.domain.user.service.LoginService;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.LoginForm;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;

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
