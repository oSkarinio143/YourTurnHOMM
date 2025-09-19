package pl.oskarinio.yourturnhomm.app.business.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.out.UserRepository;
import pl.oskarinio.yourturnhomm.domain.port.user.Admin;
import pl.oskarinio.yourturnhomm.domain.usecase.user.AdminUseCase;

import java.util.List;

@Service
@Slf4j
public class AdminService implements Admin {
    private final AdminUseCase adminUseCase;

    public AdminService(UserRepository userRepository, @Value("${spring.profiles.active:}") String[] activeProfiles) {
        this.adminUseCase = new AdminUseCase(userRepository, activeProfiles);
    }

    @Override
    public List<User> getUserList() {
        log.trace("Zwracam listę użytkowników");
        return adminUseCase.getUserList();
    }

    @Override
    public void deleteUser(String username) {
        log.debug("Usuwanie użytkownika. Nazwa = {}", username);
        adminUseCase.deleteUser(username);
    }

    @Override
    public void grantAdminRole(String username) {
        log.debug("Nadawanie roli administratora użytkownikowi. Nazwa = {}", username);
        adminUseCase.grantAdminRole(username);
    }
}
