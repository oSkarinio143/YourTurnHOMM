package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
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

   @Transactional
    public void deleteUser(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        if(!user.getUsername().equals(System.getenv("ADMIN_USERNAME")))
            userRepository.delete(user);
    }

    @Transactional
    public void grantAdminRole(String username){
        User user = userRepository.findByUsernameOrThrow(username);
        if(!user.getRoles().contains(Role.ROLE_ADMIN))
            user.addRole(Role.ROLE_ADMIN);
    }
}
