package pl.oskarinio.yourturnhomm.app.user.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface RegisterUseCase {
    UserServiceData registerUser(RegisterForm registerForm);
}
