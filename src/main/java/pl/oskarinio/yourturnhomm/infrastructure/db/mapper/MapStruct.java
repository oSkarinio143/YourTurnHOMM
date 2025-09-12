package pl.oskarinio.yourturnhomm.infrastructure.db.mapper;

import org.mapstruct.Mapper;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.model.form.LoginForm;
import pl.oskarinio.yourturnhomm.domain.model.form.RegisterForm;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelFormRequest;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginFormRequest;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterFormRequest;

@Mapper(componentModel = "spring")
public interface MapStruct {

    LoginForm toLoginForm(LoginFormRequest loginFormRequest);
    RegisterForm toRegisterForm(RegisterFormRequest registerFormRequest);
    DuelForm toDuelForm(DuelFormRequest duelFormRequest);

}
