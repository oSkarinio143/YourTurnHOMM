package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import oskarinio143.heroes3.model.Unit;
import oskarinio143.heroes3.repository.UnitRepository;

@Service
public class DuelService {

    private final DatabaseService databaseService;
    private final UnitRepository unitRepository;
    private final BattleService battleService;

    public DuelService(DatabaseService databaseService, UnitRepository unitRepository, BattleService battleService) {
        this.databaseService = databaseService;
        this.unitRepository = unitRepository;
        this.battleService = battleService;
    }
    public void prepareUnits(Model model, Unit currentLeftUnit, Unit currentRightUnit){
        model.addAttribute("leftUnit", currentLeftUnit);
        model.addAttribute("rightUnit", currentRightUnit);
    }

    public void selectUnit(Model model, String side, Unit currentLeftUnit, Unit currentRightUnit){
        databaseService.viewUnits(model);
        if (currentLeftUnit != null){
            model.addAttribute("currentLeftUnit", currentLeftUnit.getName());
        }
        if(currentRightUnit != null){
            model.addAttribute("currentRightUnit", currentRightUnit.getName());
        }
        model.addAttribute("side", side);
    }

    public void loadUnit(Model model, String name, String side, Unit leftUnit, Unit rightUnit, String leftUnitName, String rightUnitName){
        Unit unit = databaseService.getUnitByName(name);
        if(side.equals("left")){
            model.addAttribute("leftUnit", unit);
            model.addAttribute("rightUnit", rightUnit);
        } else if (side.equals("right")) {
            model.addAttribute("rightUnit", unit);
            model.addAttribute("leftUnit", leftUnit);
        }

        if(leftUnitName != null && rightUnitName != null){
            model.addAttribute("leftUnit", unitRepository.getReferenceById(leftUnitName));
            model.addAttribute("rightUnit", unitRepository.getReferenceById(rightUnitName));
        }
    }

    public void loadBattle(Unit leftUnit, Unit rightUnit, int leftQuantity, int rightQuantity, Model model, String userUUID){
        model.addAttribute("leftUnit", leftUnit);
        model.addAttribute("rightUnit", rightUnit);
        model.addAttribute("leftQuantity", leftQuantity);
        model.addAttribute("rightQuantity", rightQuantity);
        battleService.startBattle(leftUnit, rightUnit, leftQuantity, rightQuantity, userUUID);
    }
}
