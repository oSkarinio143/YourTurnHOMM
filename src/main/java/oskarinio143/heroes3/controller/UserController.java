package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.config.CookieHelper;
import oskarinio143.heroes3.exception.UserAlreadyExistsInDatabase;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.Route;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.repository.UserRepository;
import oskarinio143.heroes3.service.RegisterService;
import oskarinio143.heroes3.service.UserService;

@RequestMapping(Route.MAIN)
@Controller
public class UserController {

    private final UserService userService;
    private final RegisterService registerService;
    private final UserRepository userRepository;
    private final CookieHelper cookieHelper;

    public UserController(UserService userService, RegisterService registerService, UserRepository userRepository, CookieHelper cookieHelper) {
        this.userService = userService;
        this.registerService = registerService;
        this.userRepository = userRepository;
        this.cookieHelper = cookieHelper;
    }

    @GetMapping(Route.LOGIN)
    public String loginView(){
        return "login.html";
    }

    @GetMapping(Route.REGISTER)
    public String registerView(){
        return"register.html";
    }

    @PostMapping(Route.LOGIN)
    public String login(@ModelAttribute LoginForm loginForm){
        return "";
    }

    @PostMapping(Route.REGISTER)
    public String register(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletResponse response){
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", registerService.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.REGISTER;
        }
        try{
            UserServiceData userServiceData = registerService.registerUser(loginForm);
            cookieHelper.setCookieTokens(userServiceData, response);
            redirectAttributes.addFlashAttribute("registerMessage", "Udało się zarejestrować użytkownika");
            return Route.REDIRECT;
        }catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Użytkownik istnieje już w bazie danych");
            return Route.REDIRECT + Route.REGISTER;
        }
    }
}
