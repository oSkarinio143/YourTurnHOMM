package pl.oskarinio.yourturnhomm.domain.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class LoginForm {

    private String username;
    private String password;

}