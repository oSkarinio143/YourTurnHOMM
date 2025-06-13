package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import oskarinio143.heroes3.Unit;
import oskarinio143.heroes3.UnitRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DuelService {

    private final DatabaseService databaseService;
    private final UnitRepository unitRepository;

    public DuelService(DatabaseService databaseService, UnitRepository unitRepository) {
        this.databaseService = databaseService;
        this.unitRepository = unitRepository;
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

    public void loadUnit(Model model, String name, String side, Unit leftUnit, Unit rightUnit){
        Unit unit = databaseService.getUnitByName(name);
        if(side.equals("left")){
            model.addAttribute("leftUnit", unit);
            model.addAttribute("rightUnit", rightUnit);
        } else if (side.equals("right")) {
            model.addAttribute("rightUnit", unit);
            model.addAttribute("leftUnit", leftUnit);
        }
    }
}
