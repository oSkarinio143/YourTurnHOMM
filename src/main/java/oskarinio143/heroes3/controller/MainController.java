package oskarinio143.heroes3.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("oskarinio143/heroes")
public class MainController {

    @GetMapping()
    public String welcome(){
        return "choseMode";
    }
}
