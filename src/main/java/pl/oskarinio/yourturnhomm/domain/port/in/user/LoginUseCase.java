package pl.oskarinio.yourturnhomm.domain.port.in.user;

import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;

public interface LoginUseCase {
    UserServiceData loginUser(LoginForm loginForm);
}
