package pl.oskarinio.yourturnhomm.app.user.port.in;

import pl.oskarinio.yourturnhomm.domain.model.entity.User;

import java.util.List;

public interface AdminUseCase {
    List<User> getUserList();
    void deleteUser(String username);
    void grantAdminRole(String username);
}
