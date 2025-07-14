package oskarinio143.heroes3.model.servicedto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import oskarinio143.heroes3.model.constant.Role;

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
    private List<Role> roles = new ArrayList<>();
    private String accessToken;
    private String refreshToken;

    public void addRole(Role role){
        roles.add(role);
    }
}
