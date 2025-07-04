package oskarinio143.heroes3.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotBlank(message = "Nazwa użytkownika jest wymagana, nie może pozostać pusta")
    private String username;
    @NotBlank(message = "Hasło jest wymagane, nie może pozostać puste")
    private String password;
}
