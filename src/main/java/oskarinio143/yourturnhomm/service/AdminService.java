package oskarinio143.yourturnhomm.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oskarinio143.yourturnhomm.model.constant.Role;
import oskarinio143.yourturnhomm.model.entity.User;
import oskarinio143.yourturnhomm.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final List<String> activeProfiles;

    public AdminService(UserRepository userRepository,
                        @Value("${spring.profiles.active:}") String[] activeProfiles) {
        this.userRepository = userRepository;
        this.activeProfiles = Arrays.asList(activeProfiles);
    }

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    @Transactional
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

    @Transactional
    public void grantAdminRole(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        if(!user.getRoles().contains(Role.ROLE_ADMIN))
            user.addRole(Role.ROLE_ADMIN);
    }
}
