package pl.oskarinio.yourturnhomm.domain.port.in.user;

import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;

public interface RegisterUseCase {
    UserServiceData registerUser(RegisterForm registerForm);
}
