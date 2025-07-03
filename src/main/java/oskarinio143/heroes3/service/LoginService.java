package oskarinio143.heroes3.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.Role;
import oskarinio143.heroes3.model.servicedto.LoginServiceData;
import oskarinio143.heroes3.model.entity.RefreshToken;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.model.servicedto.LoginValidationData;
import oskarinio143.heroes3.repository.RefreshTokenRepository;
import oskarinio143.heroes3.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginValidationService loginValidationService;


    public LoginService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, LoginValidationService loginValidationService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.loginValidationService = loginValidationService;
    }

    @Transactional
    public String registerUser(LoginForm loginForm, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        LoginValidationData loginValidationData = loginValidationService.getLoginValidationData(redirectAttributes, loginForm);
        if(loginValidationService.whetherPassValidation(loginValidationData))
            userRegisterConfig(loginForm, response);
        return loginValidationData.getNextView();
    }

    public void userRegisterConfig(LoginForm loginForm, HttpServletResponse response){
        LoginServiceData loginServiceData = getLoginServiceData(loginForm);
        getTokens(loginServiceData);
        setCookieTokens(loginServiceData, response);
        saveData(loginServiceData);
    }

    public LoginServiceData getLoginServiceData(LoginForm loginForm){
        String hashedPassword = passwordEncoder.encode(loginForm.getPassword());
        LoginServiceData loginServiceData = new LoginServiceData(loginForm.getUsername(), hashedPassword);
        loginServiceData.addRole(Role.ROLE_USER.name());
        return loginServiceData;
    }

    public void getTokens(LoginServiceData loginServiceData){
        loginServiceData.setAccessToken(tokenService.generateToken(loginServiceData));
        loginServiceData.setRefreshToken(tokenService.generateToken(loginServiceData));
    }

    public void setCookieTokens(LoginServiceData loginServiceData, HttpServletResponse response){
        setCookieAccessToken(response, loginServiceData.getAccessToken());
        setCookieRefreshToken(response,loginServiceData.getRefreshToken());
    }

    public void setCookieAccessToken(HttpServletResponse response, String token){
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void setCookieRefreshToken(HttpServletResponse response, String token){
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Transactional
    public void saveData(LoginServiceData loginServiceData){
        RefreshToken refreshToken = new RefreshToken(loginServiceData.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        User user = new User(loginServiceData.getUsername(), loginServiceData.getPassword());
        user.setRoles(loginServiceData.getRoles());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
