package pl.oskarinio.yourturnhomm.app.business.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.out.PasswordEncoderPort;
import pl.oskarinio.yourturnhomm.domain.port.user.Login;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.domain.usecase.user.LoginUseCase;

@Service
@Slf4j
public class LoginService implements Login {
    private final LoginUseCase loginUseCase;

    public LoginService(UserManagement userManagement, PasswordEncoderPort passwordEncoderPort) {
        this.loginUseCase = new LoginUseCase(userManagement, passwordEncoderPort);
    }

    @Transactional
    @Override
    public UserServiceData loginUser(LoginForm loginForm) {
        log.debug("Rozpoczynam proces logowania. Nazwa = {}", loginForm.getUsername());
        return loginUseCase.loginUser(loginForm);
    }
}
