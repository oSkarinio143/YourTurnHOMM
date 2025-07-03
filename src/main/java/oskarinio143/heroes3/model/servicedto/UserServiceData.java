package oskarinio143.heroes3.model.servicedto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class UserServiceData {
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
