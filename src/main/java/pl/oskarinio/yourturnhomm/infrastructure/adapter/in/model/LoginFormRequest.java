package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFormRequest {

    @NotBlank(message = "Nazwa użytkownika jest wymagana, nie może pozostać pusta")
    private String username;

    @NotBlank(message = "Hasło jest wymagane, nie może pozostać puste")
    private String password;
}

