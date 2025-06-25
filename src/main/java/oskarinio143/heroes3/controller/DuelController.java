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
        duelService.prepareUnits(model, duelInfo.getLeftUnit(), duelInfo.getRightUnit());
        return "duel";
    }

    @PostMapping("/select")
    public String selectUnit(Model model, @ModelAttribute DuelInfo duelInfo){
        duelService.selectUnit(model, duelInfo.getSide(), duelInfo.getLeftUnit(), duelInfo.getRightUnit());
        return "select";
    }

    @PostMapping()
    public String loadUnit(Model model,
                          @RequestParam String name,
                          @RequestParam String side,
                          @RequestParam(required = false) Unit currentLeftUnit,
                          @RequestParam(required = false) Unit currentRightUnit,
                          @RequestParam(required = false) String leftUnitName,
                          @RequestParam(required = false) String rightUnitName){
        duelService.loadUnit(model, name, side, currentLeftUnit, currentRightUnit, leftUnitName, rightUnitName);
        return "duel";
    }

    @PostMapping("/battle")
    public String startBattle(Model model,
                              @RequestParam Unit leftUnit,
                              @RequestParam Unit rightUnit,
                              @RequestParam int leftQuantity,
                              @RequestParam int rightQuantity,
                              @RequestParam String userUUID){
        duelService.loadBattle(leftUnit, rightUnit, leftQuantity, rightQuantity, model, userUUID);
        return "battle";
    }
}
