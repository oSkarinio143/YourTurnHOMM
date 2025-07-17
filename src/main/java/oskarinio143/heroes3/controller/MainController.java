package oskarinio143.heroes3.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.constant.Route;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
public class MainController {

    @GetMapping()
    public String welcome(HttpServletRequest request, Model model){
        Object errorMessage = request.getAttribute("adminErrorMessage");
        if (errorMessage != null) {
            model.addAttribute("adminErrorMessage", errorMessage.toString());
        }
        return Route.VIEW_MODE;
    }
}
