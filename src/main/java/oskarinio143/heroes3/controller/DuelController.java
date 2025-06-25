package oskarinio143.heroes3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import oskarinio143.heroes3.model.DuelInfo;
import oskarinio143.heroes3.model.Unit;
import oskarinio143.heroes3.service.DuelService;

@Controller
@RequestMapping("/oskarinio143/heroes/duel")
@CrossOrigin(origins = "*")
public class DuelController {

    private final DuelService duelService;

    public DuelController(DuelService duelService) {
        this.duelService = duelService;
    }

    @GetMapping
    public String prepareDuel(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.prepareUnits(model, duelInfo);
        return "duel";
    }

    @PostMapping("/select")
    public String selectUnit(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.selectUnit(model, duelInfo);
        return "select";
    }

    @PostMapping()
    public String loadUnit(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.loadUnit(model, duelInfo);
        return "duel";
    }

    @PostMapping("/battle")
    public String startBattle(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.loadBattle(model, duelInfo);
        return "battle";
    }
}
