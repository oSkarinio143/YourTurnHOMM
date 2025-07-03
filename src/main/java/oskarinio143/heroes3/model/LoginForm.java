package oskarinio143.heroes3.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oskarinio143.heroes3.validation.ValidSamePassword;

@NoArgsConstructor
@Getter
@Setter
@ValidSamePassword
public class LoginForm {
    @NotBlank(message = "Nazwa użytkownika jest wymagana, nie może pozostać pusty")
    @Size(min = 4, max = 16, message = "Nazwa użytkownika musi mieć od 4 do 16 znaków")
    private String username;
    @NotBlank(message = "Hasło jest wymagane, nie może pozostać puste")
    @Size(min = 4, max = 16, message = "Hasło musi mieć od 4 do 16 znaków")
    private String password;

    private String confirmPassword;
}
