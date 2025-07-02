package oskarinio143.heroes3.model.servicedto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginServiceData {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private List<String> roles = new ArrayList<>();
    private String accessToken;
    private String refreshToken;

    public void addRole(String role){
        roles.add(role);
    }
}
