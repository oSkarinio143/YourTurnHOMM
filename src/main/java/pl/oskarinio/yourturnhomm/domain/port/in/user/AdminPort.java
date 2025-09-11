package pl.oskarinio.yourturnhomm.domain.port.in.user;


import pl.oskarinio.yourturnhomm.domain.model.user.User;

import java.util.List;

public interface AdminPort {
    List<User> getUserList();
    void deleteUser(String username);
    void grantAdminRole(String username);
}
