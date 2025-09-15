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
import pl.oskarinio.yourturnhomm.app.technical.communication.CookieHelperService;
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
class UserController {

    private final UserManagement userManagement;
    private final Register register;
    private final Login login;
    private final CookieHelperService cookieHelperService;
    private final MapStruct mapper;

    public UserController(UserManagement userManagement, Register register, Login login, CookieHelperService cookieHelperService, MapStruct mapper) {
        this.userManagement = userManagement;
        this.register = register;
        this.login = login;
        this.cookieHelperService = cookieHelperService;
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

        log.info("Uzytkownik probuje sie zalogowac");
        if (bindingResult.hasErrors()){
            log.warn("Logowanie nie udane");
            List<String> errorMessages = bindingResult.getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList();
            redirectAttributes.addFlashAttribute("errorMessage", userManagement.prepareErrorMessage(errorMessages));
            return Route.REDIRECT + Route.LOGIN;
        }
        UserServiceData userServiceData = login.loginUser(mapper.toLoginForm(loginFormRequest));
        cookieHelperService.setCookieTokens(userServiceData, response);
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

        log.info("Uzytkownik probuje sie zarejestrowac");
        if(bindingResult.hasErrors()) {
            log.warn("Rejestracja nie udana");
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(v -> v.getDefaultMessage())
                    .toList();
            redirectAttributes.addFlashAttribute("errorMessage", userManagement.prepareErrorMessage(errorMessages));
            return Route.REDIRECT + Route.REGISTER;
        }
        UserServiceData userServiceData = register.registerUser(mapper.toRegisterForm(registerFormRequest));
        cookieHelperService.setCookieTokens(userServiceData, response);
        redirectAttributes.addFlashAttribute("welcomeUserMessage", "Udało się zarejestrować użytkownika");
        log.info("Uzytkownik zostal zarejestrowany");
        return Route.REDIRECT;
    }

    @PostMapping(Route.LOGOUT)
    public String logoutUser(RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request){
        String username = cookieHelperService.getUsernameFromCookie(request);
        cookieHelperService.removeAccessCookie(response);
        cookieHelperService.removeRefreshCookie(response);
        userManagement.deleteToken(username);
        redirectAttributes.addFlashAttribute("logoutMessage", "Użytkownik został wylogowany");
        log.info("Uzytkownik zostal wylogowany");
        return Route.REDIRECT + Route.LOGIN;
    }
}
