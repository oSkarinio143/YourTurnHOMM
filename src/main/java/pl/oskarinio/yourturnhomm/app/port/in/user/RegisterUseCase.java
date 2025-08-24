package pl.oskarinio.yourturnhomm.app.port.in.user;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

public interface RegisterUseCase {
    UserServiceData registerUser(RegisterForm registerForm);
}
