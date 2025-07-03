package oskarinio143.heroes3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequestMapping("oskarinio143/heroes")
public class MainController {

    @GetMapping()
    public String welcome(){
        return "choseMode";
    }
}
