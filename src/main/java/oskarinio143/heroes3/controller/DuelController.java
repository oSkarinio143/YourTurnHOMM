package oskarinio143.heroes3.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import oskarinio143.heroes3.model.constant.Route;
import oskarinio143.heroes3.model.servicedto.DuelInfo;
import oskarinio143.heroes3.service.DuelService;

@Controller
@RequestMapping(Route.MAIN + Route.DUEL)
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

    @PostMapping(Route.SELECT)
    public String selectUnit(Model model, @ModelAttribute DuelInfo duelInfo, @RequestParam String side){
        duelService.selectUnit(model, duelInfo, side);
        return "select";
    }

    @PostMapping()
    public String loadUnit(Model model, @ModelAttribute DuelInfo duelInfo, @RequestParam String side, @RequestParam String name){
        duelService.loadUnit(model, duelInfo, side, name);
        return "duel";
    }

    @PostMapping(Route.BATTLE)
    public String startBattle(Model model, @Valid @ModelAttribute DuelInfo duelInfo){
        duelService.loadBattle(model, duelInfo);
        return "battle";
    }
}
