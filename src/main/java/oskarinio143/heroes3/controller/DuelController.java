package oskarinio143.heroes3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.service.DuelService;

import java.util.List;

@Controller
@RequestMapping("/oskarinio143/heroes/duel")
public class DuelController {

    private final DuelService duelService;

    public DuelController(DuelService duelService) {
        this.duelService = duelService;
    }

    @GetMapping
    public String duelBoard(){
        return "duel";
    }

    @PostMapping("/select")
    public String selectUnit(Model model, @RequestParam String side){
        duelService.selectUnit(model, side);
        return "select";
    }

    @PostMapping()
    public String getUnit(Model model, @RequestParam String name, @RequestParam String side){
        duelService.loadUnit(model, name, side);
        return "duel";
    }
}
