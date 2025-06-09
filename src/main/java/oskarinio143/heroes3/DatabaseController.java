package oskarinio143.heroes3;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/heroes/database")
public class DatabaseController {

    @GetMapping
    public String choseDatabaseOption(){
        return "choseDatabaseOption";
    }
}
