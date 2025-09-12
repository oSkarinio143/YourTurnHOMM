package pl.oskarinio.yourturnhomm.domain.port.user;

import pl.oskarinio.yourturnhomm.domain.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface Register {
    UserServiceData registerUser(RegisterForm registerForm);
}
