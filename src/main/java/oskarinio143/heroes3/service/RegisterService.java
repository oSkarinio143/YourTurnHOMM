package oskarinio143.heroes3.service;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import oskarinio143.heroes3.exception.UserAlreadyExistsInDatabase;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RegisterService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Transactional
    public UserServiceData registerUser(LoginForm loginForm){
        UserServiceData userServiceData = userService.getUserServiceData(loginForm);
        userService.generateAndSetTokens(userServiceData);
        saveData(userServiceData);
        return userServiceData;
    }

    @Transactional
    public void saveData(UserServiceData loginServiceData){
//        if(userRepository.findByUsername(loginServiceData.getUsername()).isPresent())
//            throw new UserAlreadyExistsInDatabase();

        RefreshToken refreshToken = new RefreshToken(loginServiceData.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        User user = new User(loginServiceData.getUsername(), loginServiceData.getPassword());
        user.setRoles(loginServiceData.getRoles());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public String prepareErrorMessage(List<ObjectError> errorsMessageList){
        StringBuilder errorMessage = new StringBuilder();
        errorsMessageList.forEach(v -> errorMessage.append(v.getDefaultMessage() + "<br>"));
        return errorMessage.toString();
    }
}
