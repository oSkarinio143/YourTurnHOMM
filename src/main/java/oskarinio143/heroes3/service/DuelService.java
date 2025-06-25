package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import oskarinio143.heroes3.model.DuelInfo;
import oskarinio143.heroes3.model.Unit;
import oskarinio143.heroes3.repository.UnitRepository;

import java.util.Optional;

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
    public void prepareUnits(Model model, DuelInfo duelInfo){
        model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        model.addAttribute("rightUnit", duelInfo.getRightUnit());
    }

    public void selectUnit(Model model, DuelInfo duelInfo){
        databaseService.viewUnits(model);
        if (duelInfo.getLeftUnit() != null){
            model.addAttribute("leftUnit", duelInfo.getLeftUnit().getName());
        }
        if(duelInfo.getRightUnit() != null){
            model.addAttribute("rightUnit", duelInfo.getRightUnit().getName());
        }
        model.addAttribute("side", duelInfo.getSide());
    }

    public void loadUnit(Model model, DuelInfo duelInfo){
        Unit unit = unitRepository.getReferenceById(duelInfo.getName());

        if(duelInfo.getLeftUnit() != null && duelInfo.getRightUnit() != null){
            model.addAttribute("leftUnit", unitRepository.getReferenceById(duelInfo.getLeftUnit().getName()));
            model.addAttribute("rightUnit", unitRepository.getReferenceById(duelInfo.getRightUnit().getName()));
        }


        if(duelInfo.getSide().equals("left")){
            model.addAttribute("leftUnit", unit);
            model.addAttribute("rightUnit", duelInfo.getRightUnit());
        } else if (duelInfo.getSide().equals("right")) {
            model.addAttribute("rightUnit", unit);
            model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        }
    }

    public void loadBattle(Model model, DuelInfo duelInfo){
        model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        model.addAttribute("rightUnit", duelInfo.getRightUnit());
        model.addAttribute("leftQuantity", duelInfo.getLeftQuantity());
        model.addAttribute("rightQuantity", duelInfo.getRightQuantity());
        battleService.prepareBattle(duelInfo.getLeftUnit(), duelInfo.getRightUnit(), duelInfo.getLeftQuantity(), duelInfo.getRightQuantity(), duelInfo.getName());
    }
}
