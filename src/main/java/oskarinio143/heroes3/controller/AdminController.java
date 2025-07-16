package oskarinio143.heroes3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.entity.User;
import oskarinio143.heroes3.service.AdminService;

import java.util.List;

@Controller
@RequestMapping(Route.MAIN + Route.ADMIN)
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
    public String deleteUserView(Model model){
        model.addAttribute("adminUsername", System.getenv("ADMIN_USERNAME"));
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
