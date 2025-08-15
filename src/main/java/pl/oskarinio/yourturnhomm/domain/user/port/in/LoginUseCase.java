package pl.oskarinio.yourturnhomm.domain.user.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.LoginForm;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;

public interface LoginUseCase {
    UserServiceData loginUser(LoginForm loginForm);
}
