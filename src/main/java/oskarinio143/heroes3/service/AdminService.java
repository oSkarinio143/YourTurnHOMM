package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import oskarinio143.heroes3.model.constant.Role;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.repository.UserRepository;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public void deleteUser(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        userRepository.delete(user);
    }

    public void grantAdminRole(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        user.addRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}
