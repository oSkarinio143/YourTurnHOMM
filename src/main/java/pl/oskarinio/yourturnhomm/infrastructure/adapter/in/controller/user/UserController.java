package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.rest.service.CookieHelperAdapter;
import pl.oskarinio.yourturnhomm.app.user.port.in.LoginUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.in.RegisterUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.in.UserUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;

@RequestMapping(Route.MAIN)
@Controller
public class UserController {

    private final UserUseCase userUseCase;
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final CookieHelperAdapter cookieHelperAdapter;

    public UserController(UserUseCase userUseCase, RegisterUseCase registerUseCase, LoginUseCase loginUseCase, CookieHelperAdapter cookieHelperAdapter) {
        this.userUseCase = userUseCase;
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
        this.cookieHelperAdapter = cookieHelperAdapter;
    }

    @GetMapping(Route.LOGIN)
    public String loginView(Model model,
                            @RequestParam(name = "logout", required = false) String logoutType){

        if(logoutType != null && logoutType.equals("auto"))
            model.addAttribute("autoLogoutMessage", "Zostałeś automatycznie wylogowany z powodu braku aktywności");
        return Route.PACKAGE_CONNECTION + Route.LOGIN;
    }

    @PostMapping(Route.LOGIN)
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", userUseCase.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.LOGIN;
        }

        UserServiceData userServiceData = loginUseCase.loginUser(loginForm);
        cookieHelperAdapter.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage","Udało się poprawnie zalogować użytkownika");
        return Route.REDIRECT;
    }

    @GetMapping(Route.REGISTER)
    public String registerView(){
        return Route.PACKAGE_CONNECTION + Route.REGISTER;
    }

    @PostMapping(Route.REGISTER)
    public String register(@Valid @ModelAttribute RegisterForm registerForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpServletResponse response){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", userUseCase.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.REGISTER;
        }
        UserServiceData userServiceData = registerUseCase.registerUser(registerForm);
        cookieHelperAdapter.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage", "Udało się zarejestrować użytkownika");
        return Route.REDIRECT;
    }

    @PostMapping(Route.LOGOUT)
    public String logoutUser(RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request){
        String username = cookieHelperAdapter.getUsernameFromCookie(request);
        cookieHelperAdapter.removeAccessCookie(response);
        cookieHelperAdapter.removeRefreshCookie(response);
        userUseCase.deleteToken(username);
        redirectAttributes.addFlashAttribute("logoutMessage", "Użytkownik został wylogowany");
        return Route.REDIRECT + Route.LOGIN;
    }
}
