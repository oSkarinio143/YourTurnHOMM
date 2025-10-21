package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.oskarinio.yourturnhomm.app.technology.communication.CookieHelper;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.UserServiceData;
import pl.oskarinio.yourturnhomm.domain.port.user.Login;
import pl.oskarinio.yourturnhomm.domain.port.user.Register;
import pl.oskarinio.yourturnhomm.domain.port.user.UserManagement;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.LoginFormRequest;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.RegisterFormRequest;
import pl.oskarinio.yourturnhomm.infrastructure.db.mapper.MapStruct;

import java.util.List;

@Slf4j
@RequestMapping(Route.MAIN)
@Controller
public class UserController {

    private final UserManagement userManagement;
    private final Register register;
    private final Login login;
    private final CookieHelper cookieHelper;
    private final MapStruct mapper;

    public UserController(UserManagement userManagement, Register register, Login login, CookieHelper cookieHelper, MapStruct mapper) {
        this.userManagement = userManagement;
        this.register = register;
        this.login = login;
        this.cookieHelper = cookieHelper;
        this.mapper = mapper;
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
    public String login(@Valid @ModelAttribute LoginFormRequest loginFormRequest,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {

        log.info("Uzytkownik rozpoczyna logowanie");
        if (bindingResult.hasErrors()){
            log.warn("Logowanie nie udane, wprowadzono niepoprawne dane");
            List<String> errorMessages = bindingResult.getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList();
            redirectAttributes.addFlashAttribute("errorMessage", userManagement.prepareErrorMessage(errorMessages));
            return Route.REDIRECT + Route.LOGIN;
        }
        UserServiceData userServiceData = login.loginUser(mapper.toLoginForm(loginFormRequest));
        cookieHelper.setCookieTokens(userServiceData, response);
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
    public String register(@Valid @ModelAttribute RegisterFormRequest registerFormRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           HttpServletResponse response){

        log.info("Uzytkownik rozpoczyna rejestracje");
        if(bindingResult.hasErrors()) {
            log.warn("Rejestracja nie udana, wprowadzono niepoprawne dane");
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(v -> v.getDefaultMessage())
                    .toList();
            redirectAttributes.addFlashAttribute("errorMessage", userManagement.prepareErrorMessage(errorMessages));
            return Route.REDIRECT + Route.REGISTER;
        }
        UserServiceData userServiceData = register.registerUser(mapper.toRegisterForm(registerFormRequest));
        cookieHelper.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage", "Udało się zarejestrować użytkownika");
        log.info("Uzytkownik zostal zarejestrowany");
        return Route.REDIRECT;
    }

    @PostMapping(Route.LOGOUT)
    public String logoutUser(RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request){

        log.info("Uzytkownik rozpoczyna wylogowanie");
        String username = cookieHelper.getUsernameFromCookie(request);
        cookieHelper.removeAccessCookie(response);
        cookieHelper.removeRefreshCookie(response);
        userManagement.deleteToken(username);
        redirectAttributes.addFlashAttribute("logoutMessage", "Użytkownik został wylogowany");
        log.info("Uzytkownik zostal wylogowany");
        return Route.REDIRECT + Route.LOGIN;
    }
}
