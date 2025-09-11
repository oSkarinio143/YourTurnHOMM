package pl.oskarinio.yourturnhomm.domain.model.user;

import lombok.*;
import pl.oskarinio.yourturnhomm.infrastructure.temp.RefreshToken;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private Instant registrationDate;
    private Set<Role> roles = new HashSet<>();
    private RefreshToken refreshToken;

    public void addRole(Role role){
        roles.add(role);
    }
}
