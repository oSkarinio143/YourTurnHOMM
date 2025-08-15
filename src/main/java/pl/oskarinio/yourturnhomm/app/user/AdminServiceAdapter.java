package pl.oskarinio.yourturnhomm.app.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.user.service.AdminService;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.UserRepository;

import java.util.List;

@Service
public class AdminServiceAdapter implements pl.oskarinio.yourturnhomm.domain.user.port.in.AdminUseCase {
    private final AdminService adminService;

    public AdminServiceAdapter(UserRepository userRepository, @Value("${spring.profiles.active:}") String[] activeProfiles) {
        this.adminService = new AdminService(userRepository, activeProfiles);
    }

    @Override
    public List<User> getUserList() {
        return adminService.getUserList();
    }

    @Override
    public void deleteUser(String username) {
        adminService.deleteUser(username);
    }

    @Override
    public void grantAdminRole(String username) {
        adminService.grantAdminRole(username);
    }
}
