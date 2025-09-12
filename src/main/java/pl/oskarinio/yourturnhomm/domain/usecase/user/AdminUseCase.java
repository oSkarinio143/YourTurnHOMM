package pl.oskarinio.yourturnhomm.domain.usecase.user;

import pl.oskarinio.yourturnhomm.domain.model.exception.UsernameNotFoundException;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminUseCase {
    private final UserRepository userRepository;
    private final List<String> activeProfiles;

    public AdminUseCase(UserRepository userRepository, String[] activeProfiles) {
        this.userRepository = userRepository;
        this.activeProfiles = Arrays.asList(activeProfiles);
    }

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public void deleteUser(String username){
        Optional<User> userOpt = userRepository.findByUsername(username);
        if(!userOpt.isPresent())
            throw new UsernameNotFoundException();

        User user = userOpt.get();
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
        Optional<User> userOpt = userRepository.findByUsername(username);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            if(!user.getRoles().contains(Role.ROLE_ADMIN)) {
                user.addRole(Role.ROLE_ADMIN);
            }
            userRepository.save(user);
        }
    }
}
