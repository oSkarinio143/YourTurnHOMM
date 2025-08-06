package oskarinio143.yourturnhomm.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import oskarinio143.yourturnhomm.helper.CookieHelper;
import oskarinio143.yourturnhomm.model.constant.Route;
import oskarinio143.yourturnhomm.model.entity.User;
import oskarinio143.yourturnhomm.service.AdminService;

import java.util.List;

@Controller
@RequestMapping(Route.MAIN + Route.ADMIN)
public class AdminController {

    private final AdminService adminService;
    private final CookieHelper cookieHelper;

    @Value("${app.security.admin-username:}")
    private String adminUsername;

    public AdminController(AdminService adminService, CookieHelper cookieHelper) {
        this.adminService = adminService;
        this.cookieHelper = cookieHelper;
    }

    @ModelAttribute("users")
    public List<User> addUsersListModel(){
        List<User> userList = adminService.getUserList();
        return adminService.getUserList();
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
        model.addAttribute("thisUsername", cookieHelper.getUsernameFromCookie(request));
        return Route.PACKAGE_ADMIN + Route.VIEW_DELETE_USER;
    }

    @PostMapping(Route.DELETE)
    public String deleteUser(@RequestParam String username){
        adminService.deleteUser(username);
        return Route.REDIRECT + Route.ADMIN + Route.DELETE;
    }

    @GetMapping(Route.GRANT)
    public String grantAdminView(){
        return Route.PACKAGE_ADMIN + Route.VIEW_GRANT_ADMIN;
    }

    @PostMapping(Route.GRANT)
    public String grantAdminView(@RequestParam String username){
        adminService.grantAdminRole(username);
        return Route.REDIRECT + Route.ADMIN + Route.GRANT;
    }
}
