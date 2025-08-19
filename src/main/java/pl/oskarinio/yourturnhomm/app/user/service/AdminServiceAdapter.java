package pl.oskarinio.yourturnhomm.app.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.app.user.port.in.AdminUseCase;
import pl.oskarinio.yourturnhomm.domain.service.user.AdminService;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;

import java.util.List;

@Service
public class AdminServiceAdapter implements AdminUseCase {
    private final AdminService adminService;

    public AdminServiceAdapter(UserRepositoryPort userRepository, @Value("${spring.profiles.active:}") String[] activeProfiles) {
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
