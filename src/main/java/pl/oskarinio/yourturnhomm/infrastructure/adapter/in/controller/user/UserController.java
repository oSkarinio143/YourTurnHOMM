package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.in.user.LoginUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.user.RegisterUseCase;
import pl.oskarinio.yourturnhomm.domain.port.in.user.UserUseCase;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginForm;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterForm;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.out.CookieHelperAdapter;

@Slf4j
@RequestMapping(Route.MAIN)
@Controller
class UserController {

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

        log.info("Uzytkownik w formularzu logowania");
        if(logoutType != null && logoutType.equals("auto")) {
            model.addAttribute("autoLogoutMessage", "Zostałeś automatycznie wylogowany z powodu braku aktywności");
        }
        return Route.PACKAGE_CONNECTION + Route.LOGIN;
    }

    @PostMapping(Route.LOGIN)
    public String login(@Valid @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {

        log.info("Uzytkownik probuje sie zalogowac");
        if (bindingResult.hasErrors()){
            log.warn("Logowanie nie udane");
            redirectAttributes.addFlashAttribute("errorMessage", userUseCase.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.LOGIN;
        }
        UserServiceData userServiceData = loginUseCase.loginUser(loginForm);
        cookieHelperAdapter.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage","Udało się poprawnie zalogować użytkownika");
        log.info("Uzytkownik zostal zalogowany");
        return Route.REDIRECT;
    }

    @GetMapping(Route.REGISTER)
    public String registerView(){
        log.info("Uzytkownik w formularzu rejestracji");
        return Route.PACKAGE_CONNECTION + Route.REGISTER;
    }

    @PostMapping(Route.REGISTER)
    public String register(@Valid @ModelAttribute RegisterForm registerForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpServletResponse response){

        log.info("Uzytkownik probuje sie zarejestrowac");
        if(bindingResult.hasErrors()) {
            log.warn("Rejestracja nie udana");
            redirectAttributes.addFlashAttribute("errorMessage", userUseCase.prepareErrorMessage(bindingResult.getAllErrors()));
            return Route.REDIRECT + Route.REGISTER;
        }
        UserServiceData userServiceData = registerUseCase.registerUser(registerForm);
        cookieHelperAdapter.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage", "Udało się zarejestrować użytkownika");
        log.info("Uzytkownik zostal zarejestrowany");
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
        log.info("Uzytkownik zostal wylogowany");
        return Route.REDIRECT + Route.LOGIN;
    }
}
