package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import oskarinio143.heroes3.model.LoginForm;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.service.LoginService;

@RequestMapping("/oskarinio143/heroes/")
@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("login")
    public String loginView(){
        return "login.html";
    }

    @GetMapping("register")
    public String registerView(){
        return"register.html";
    }

    @PostMapping("login")
    public String login(@ModelAttribute LoginForm loginForm){
        return "";
    }

    @PostMapping("register")
    public String register(@ModelAttribute LoginForm loginForm, HttpServletResponse response){
        return loginService.registerUser(loginForm, response);
    }
}
