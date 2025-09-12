package pl.oskarinio.yourturnhomm.app.implementation.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.Admin;
import pl.oskarinio.yourturnhomm.domain.usecase.user.AdminUseCase;

import java.util.List;

@Service
public class AdminService implements Admin {
    private final AdminUseCase adminUseCase;

    public AdminService(UserRepository userRepository, @Value("${spring.profiles.active:}") String[] activeProfiles) {
        this.adminUseCase = new AdminUseCase(userRepository, activeProfiles);
    }

    @Override
    public List<User> getUserList() {
        return adminUseCase.getUserList();
    }

    @Override
    public void deleteUser(String username) {
        adminUseCase.deleteUser(username);
    }

    @Override
    public void grantAdminRole(String username) {
        adminUseCase.grantAdminRole(username);
    }
}
