package oskarinio143.heroes3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginForm {
    private String username;
    private String password;
    private String confirmPassword;
}
