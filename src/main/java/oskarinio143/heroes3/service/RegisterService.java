package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import oskarinio143.heroes3.model.RegisterForm;
import oskarinio143.heroes3.model.Role;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

import java.util.List;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserServiceData registerUser(RegisterForm registerForm){
        UserServiceData userServiceData = getUserServiceData(registerForm);
        userService.generateAndSetTokens(userServiceData);
        saveData(userServiceData);
        return userServiceData;
    }

    public UserServiceData getUserServiceData(RegisterForm registerForm){
        String hashedPassword = passwordEncoder.encode(registerForm.getPassword());
        UserServiceData userServiceData = new UserServiceData(registerForm.getUsername(), hashedPassword);
        userServiceData.addRole(Role.ROLE_USER.name());
        return userServiceData;
    }

    @Transactional
    public void saveData(UserServiceData userServiceData){
        RefreshToken refreshToken = userService.getAndSaveRefreshToken(userServiceData.getRefreshToken());
        User user = new User(userServiceData.getUsername(), userServiceData.getPassword());
        user.setRoles(userServiceData.getRoles());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
