package pl.oskarinio.yourturnhomm.app.port.in.user;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface LoginUseCase {
    UserServiceData loginUser(LoginForm loginForm);
}
