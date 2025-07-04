package oskarinio143.heroes3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import oskarinio143.heroes3.model.Route;

@Slf4j
@Controller
@RequestMapping(Route.MAIN)
public class MainController {

    @GetMapping()
    public String welcome(){
        return "choseMode";
    }
}
