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
    private final List<String> selectedUnits = new ArrayList<>(List.of("", ""));
    private final List<Unit> units = new ArrayList<>(List.of(new Unit(), new Unit()));

    public DuelService(DatabaseService databaseService, UnitRepository unitRepository) {
        this.databaseService = databaseService;
        this.unitRepository = unitRepository;
    }

    public void loadEmptyList(Model model){
        model.addAttribute("selectedUnits", selectedUnits);
        model.addAttribute("units", units);
    }
    public void selectUnit(Model model, String side){
        if(side.equals("left")){
            selectedUnits.set(0, "here");
        } else if (side.equals("right")) {
            selectedUnits.set(1, "here");
        }
        databaseService.viewUnits(model);
    }

    public void loadUnit(Model model, String name){
        Unit unit = databaseService.getUnitByName(name);
        if(selectedUnits.get(0) == "here"){
            selectedUnits.set(0,name);
            units.set(0, unit);
        } else if (selectedUnits.get(1) == "here") {
            selectedUnits.set(1, name);
            units.set(1, unit);
        }
        model.addAttribute("selectedUnits", selectedUnits);
        model.addAttribute("units", units);
    }
}
