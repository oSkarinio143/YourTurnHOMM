package pl.oskarinio.yourturnhomm.domain.user.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.user.model.UserServiceData;

public interface RegisterUseCase {
    UserServiceData registerUser(RegisterForm registerForm);
}
