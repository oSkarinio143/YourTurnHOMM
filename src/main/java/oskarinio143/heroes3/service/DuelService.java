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

    public void selectUnit(Model model, String side){
        databaseService.viewUnits(model);
        model.addAttribute("side", side);
    }

    public void loadUnit(Model model, String name, String side){
        Unit unit = databaseService.getUnitByName(name);
        List<Unit> units = new ArrayList<>(List.of(new Unit(), new Unit()));
        if(side.equals("left"))
            units.set(0, unit);
        else if (side.equals("right"))
            units.set(1, unit);
        model.addAttribute("side", side);
        model.addAttribute("units", units);
    }
}
