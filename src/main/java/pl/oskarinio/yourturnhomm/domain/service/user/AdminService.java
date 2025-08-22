package pl.oskarinio.yourturnhomm.domain.service.user;

import pl.oskarinio.yourturnhomm.app.user.port.out.UserRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.model.user.Role;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.infrastructure.security.exception.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private final UserRepositoryPort userRepositoryPort;
    private final List<String> activeProfiles;

    public AdminService(UserRepositoryPort userRepositoryPort, String[] activeProfiles) {
        this.userRepositoryPort = userRepositoryPort;
        this.activeProfiles = Arrays.asList(activeProfiles);
    }

    public List<User> getUserList(){
        return userRepositoryPort.findAll();
    }

    public void deleteUser(String username){
        Optional<User> userOpt = userRepositoryPort.findByUsername(username);
        if(!userOpt.isPresent())
            throw new UsernameNotFoundException();

        User user = userOpt.get();
        if(activeProfiles.contains("h2")) {
            if(!user.getUsername().equals("adminUser"))
                userRepositoryPort.delete(user);
        }
        else{
            if (!user.getUsername().equals(System.getenv("ADMIN_USERNAME")))
                userRepositoryPort.delete(user);
        }
    }

    public void grantAdminRole(String username){
        Optional<User> userOpt = userRepositoryPort.findByUsername(username);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            if(!user.getRoles().contains(Role.ROLE_ADMIN)) {
                System.out.println("adminNadany");
                user.addRole(Role.ROLE_ADMIN);
            }
            userRepositoryPort.save(user);
        }
    }
}
