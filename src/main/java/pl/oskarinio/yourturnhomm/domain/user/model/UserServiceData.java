package pl.oskarinio.yourturnhomm.domain.user.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class UserServiceData {

    @NonNull
    private String username;

    @NonNull
    private String password;
    private Set<Role> roles = new HashSet<>();
    private String accessToken;
    private String refreshToken;

    public void addRole(Role role){
        roles.add(role);
    }
}
