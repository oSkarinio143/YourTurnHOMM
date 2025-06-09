package oskarinio143.heroes3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("oskarinio143/heroes")
public class MainController {

    private final RedirectService redirectService;

    public MainController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping()
    public String welcome(){
        return "choseMode";
    }
}
