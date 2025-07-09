package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.helper.CookieHelper;
import oskarinio143.heroes3.model.form.LoginForm;
import oskarinio143.heroes3.model.form.RegisterForm;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.servicedto.UserServiceData;
import oskarinio143.heroes3.service.LoginService;
import oskarinio143.heroes3.service.RegisterService;
import oskarinio143.heroes3.service.UserService;

@RequestMapping(Route.MAIN)
@Controller
public class UserController {

    private final UserService userService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final CookieHelper cookieHelper;

    public UserController(UserService userService, RegisterService registerService, LoginService loginService, CookieHelper cookieHelper) {
        this.userService = userService;
        this.registerService = registerService;
        this.loginService = loginService;
        this.cookieHelper = cookieHelper;
    }

    @GetMapping(Route.LOGIN)
    public String loginView(Model model,
                            @RequestParam(name = "logout", required = false) String logoutType){

        if(logoutType != null && logoutType.equals("auto"))
            model.addAttribute("autoLogoutMessage", "Zostałeś automatycznie wylogowany z powodu braku aktywności");
        return "login.html";
    }

    @PostMapping(Route.LOGIN)
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", userService.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.LOGIN;
        }

        UserServiceData userServiceData = loginService.loginUser(loginForm);
        cookieHelper.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage","Udało się poprawnie zalogować użytkownika");
        return Route.REDIRECT;
    }

    @GetMapping(Route.REGISTER)
    public String registerView(){
        return"register.html";
    }

    @PostMapping(Route.REGISTER)
    public String register(@Valid @ModelAttribute RegisterForm registerForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpServletResponse response){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", userService.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.REGISTER;
        }
        UserServiceData userServiceData = registerService.registerUser(registerForm);
        cookieHelper.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage", "Udało się zarejestrować użytkownika");
        return Route.REDIRECT;
    }

    @GetMapping(Route.REFRESH)
    public String refreshToken(){
        System.out.println("odswieżam");
        return Route.REDIRECT;
    }

    @PostMapping(Route.LOGOUT)
    public String logoutUser(RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request){
        String username = cookieHelper.getUsernameFromCooke(request);
        cookieHelper.removeAccessCookie(response);
        userService.deleteToken(username);
        redirectAttributes.addFlashAttribute("logoutMessage", "Użytkownik został wylogowany");
        return Route.REDIRECT + Route.LOGIN;
    }
}
