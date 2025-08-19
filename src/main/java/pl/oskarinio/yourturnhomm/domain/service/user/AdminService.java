package pl.oskarinio.yourturnhomm.domain.service.user;

import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.entity.User;
import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;

import java.util.Arrays;
import java.util.List;

public class AdminService {
    private final UserRepositoryPort userRepository;
    private final List<String> activeProfiles;

    public AdminService(UserRepositoryPort userRepository, String[] activeProfiles) {
        this.userRepository = userRepository;
        this.activeProfiles = Arrays.asList(activeProfiles);
    }

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public void deleteUser(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        if(activeProfiles.contains("h2")) {
            if(!user.getUsername().equals("adminUser"))
                userRepository.delete(user);
        }
        else{
            if (!user.getUsername().equals(System.getenv("ADMIN_USERNAME")))
                userRepository.delete(user);
        }
    }

    public void grantAdminRole(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        if(!user.getRoles().contains(Role.ROLE_ADMIN))
            user.addRole(Role.ROLE_ADMIN);
    }
}
