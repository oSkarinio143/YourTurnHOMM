package pl.oskarinio.yourturnhomm.domain.port.user;

import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface Login {
    UserServiceData loginUser(LoginForm loginForm);
}
