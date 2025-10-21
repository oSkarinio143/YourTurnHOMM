package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.User;
import pl.oskarinio.yourturnhomm.domain.port.user.Admin;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.CookieHelper;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(Route.MAIN + Route.ADMIN)
public class AdminController {

    private final Admin admin;
    private final CookieHelper cookieHelper;

    @Value("${app.security.admin-username:}")
    private String adminUsername;

    public AdminController(Admin admin, CookieHelper cookieHelper) {
        this.admin = admin;
        this.cookieHelper = cookieHelper;
    }

    //Dostarcza liste uzytkownikow kazdej metodzie w klasie
    @ModelAttribute(Route.USERS_LIST)
    public List<User> addUsersListModel(){
        List<User> userList = admin.getUserList();
        return admin.getUserList();
    }

    @GetMapping
    public String choseAdminOption(){
        log.info("Uzytkownik administrator w panelu admina");
        return Route.PACKAGE_ADMIN + Route.ADMIN;
    }

    @GetMapping(Route.SHOW)
    public String showUsers(Model model){
        log.info("Admin wyswietla liste uzytkownikow");
        return Route.PACKAGE_ADMIN + Route.VIEW_SHOW_USERS;
    }

    @GetMapping(Route.DELETE)
    public String deleteUserView(Model model, HttpServletRequest request){
        log.info("Admin wybiera uzytkownika do usuniecia");
        model.addAttribute("adminUsername", adminUsername);
        model.addAttribute("thisUsername", cookieHelper.getUsernameFromCookie(request));
        return Route.PACKAGE_ADMIN + Route.VIEW_DELETE_USER;
    }

    @PostMapping(Route.DELETE)
    public String deleteUser(@RequestParam String username){
        admin.deleteUser(username);
        log.info("Admin usunal uzytkownika");
        return Route.REDIRECT + Route.ADMIN + Route.DELETE;
    }

    @GetMapping(Route.GRANT)
    public String grantAdmin(){
        log.info("Admin wybiera uzytkownika do nadania uprawnien administracyjnych");
        return Route.PACKAGE_ADMIN + Route.VIEW_GRANT_ADMIN;
    }

    @PostMapping(Route.GRANT)
    public String grantAdmin(@RequestParam String username){
        admin.grantAdminRole(username);
        log.info("Admin nadal uzytkownikowi uprawnienia administracyjne");
        return Route.REDIRECT + Route.ADMIN + Route.GRANT;
    }
}
