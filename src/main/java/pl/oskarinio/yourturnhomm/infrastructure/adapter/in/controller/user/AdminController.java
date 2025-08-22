package pl.oskarinio.yourturnhomm.infrastructure.adapter.in.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.oskarinio.yourturnhomm.app.rest.port.in.CookieHelperUseCase;
import pl.oskarinio.yourturnhomm.app.user.port.in.AdminUseCase;
import pl.oskarinio.yourturnhomm.domain.model.Route;
import pl.oskarinio.yourturnhomm.domain.model.user.User;

import java.util.List;

@Controller
@RequestMapping(Route.MAIN + Route.ADMIN)
public class AdminController {

    private final AdminUseCase adminUseCase;
    private final CookieHelperUseCase cookieHelperUseCase;

    @Value("${app.security.admin-username:}")
    private String adminUsername;

    public AdminController(AdminUseCase adminUseCase, CookieHelperUseCase cookieHelperUseCase) {
        this.adminUseCase = adminUseCase;
        this.cookieHelperUseCase = cookieHelperUseCase;
    }

    @ModelAttribute("users")
    public List<User> addUsersListModel(){
        List<User> userList = adminUseCase.getUserList();
        return adminUseCase.getUserList();
    }

    @GetMapping
    public String choseAdminOption(){
        return Route.PACKAGE_ADMIN + Route.ADMIN;
    }

    @GetMapping(Route.SHOW)
    public String showUsers(Model model){
        return Route.PACKAGE_ADMIN + Route.VIEW_SHOW_USERS;
    }

    @GetMapping(Route.DELETE)
    public String deleteUserView(Model model, HttpServletRequest request){
        model.addAttribute("adminUsername", adminUsername);
        model.addAttribute("thisUsername", cookieHelperUseCase.getUsernameFromCookie(request));
        return Route.PACKAGE_ADMIN + Route.VIEW_DELETE_USER;
    }

    @PostMapping(Route.DELETE)
    public String deleteUser(@RequestParam String username){
        adminUseCase.deleteUser(username);
        return Route.REDIRECT + Route.ADMIN + Route.DELETE;
    }

    @GetMapping(Route.GRANT)
    public String grantAdminView(){
        return Route.PACKAGE_ADMIN + Route.VIEW_GRANT_ADMIN;
    }

    @PostMapping(Route.GRANT)
    public String grantAdminView(@RequestParam String username){
        adminUseCase.grantAdminRole(username);
        return Route.REDIRECT + Route.ADMIN + Route.GRANT;
    }
}
