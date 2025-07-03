package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.servicedto.LoginValidationData;
import oskarinio143.heroes3.repository.UserRepository;

@Service
public class LoginValidationService {
    private final UserRepository userRepository;

    public LoginValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginValidationData getLoginValidationData(RedirectAttributes redirectAttributes, LoginForm loginForm){
        return new LoginValidationData(loginForm.getUsername(), loginForm.getPassword(), loginForm.getConfirmPassword(), redirectAttributes);
    }

    public boolean whetherPassValidation(LoginValidationData loginValidationData){
        registerValidation(loginValidationData);
        return loginValidationData.getErrorMessages().isEmpty();
    }

    public void registerValidation(LoginValidationData loginValidationData) {
        checkUsernameCorrect(loginValidationData);
        checkPasswordCorrect(loginValidationData);
        completeValidation(loginValidationData);
    }

    public void checkUsernameCorrect(LoginValidationData loginValidationData) {
        if (!isMinSize(loginValidationData.getUsername().replaceAll("\\s+", ""), 4))
            loginValidationData.addErrorMessage("Minimalna długość nazwy użytkownika wynosi 4 znaki");
        if (!isMaxSize(loginValidationData.getUsername().replaceAll("\\s+", ""), 16))
            loginValidationData.addErrorMessage("Maksymalna długość nazwy użytkownika wynosi 16 znaków");
        if(isUserInDatabase(loginValidationData.getUsername()))
            loginValidationData.addErrorMessage(("Użytkownik jest już w bazie danych"));
    }

    public void checkPasswordCorrect(LoginValidationData loginValidationData) {
        if (!isMinSize(loginValidationData.getPassword().replaceAll("\\s+", ""), 4))
            loginValidationData.addErrorMessage("Minimalna długość hasła wynosi 4 znaki");
        if (!isMaxSize(loginValidationData.getPassword().replaceAll("\\s+", ""), 16))
            loginValidationData.addErrorMessage("Maksymalna długość hasła wynosi 16 znaków");
        if(!isPasswordsSame(loginValidationData.getPassword(), loginValidationData.getConfirmPassword()))
            loginValidationData.addErrorMessage("Hasła nie są takie same");
    }

    public boolean isMinSize(String text, int length) {
        return text.length() >= length;
    }

    public boolean isMaxSize(String text, int length) {
        return text.length() <= length;
    }

    public boolean isUserInDatabase(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isPasswordsSame(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    public void completeValidation(LoginValidationData loginValidationData){
        if(!loginValidationData.getErrorMessages().isEmpty()) {
            loginValidationData.setNextView("redirect:/oskarinio143/heroes/register");
            loginValidationData.getRedirectAttributes().addFlashAttribute("errorMessage", loginValidationData.getErrorMessages());
        }
        else
            loginValidationData.setNextView("redirect:/oskarinio143/heroes");
    }
}
