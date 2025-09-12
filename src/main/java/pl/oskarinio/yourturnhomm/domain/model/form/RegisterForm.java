package pl.oskarinio.yourturnhomm.domain.model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    private String username;
    private String password;
    private String confirmPassword;

}
