package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import oskarinio143.heroes3.model.DuelInfo;
import oskarinio143.heroes3.model.BattleUnit;
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
    public void prepareUnits(Model model, DuelInfo duelInfo){
        model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        model.addAttribute("rightUnit", duelInfo.getRightUnit());

//        model.addAttribute("leftQuantity", duelInfo.getLeftQuantity());
//        model.addAttribute("rightQuantity", duelInfo.getRightQuantity());
    }

    public void selectUnit(Model model, DuelInfo duelInfo, String side){
        databaseService.viewUnits(model);
        if (duelInfo.getLeftUnit() != null){
            model.addAttribute("leftUnit", duelInfo.getLeftUnit().getName());
        }
        if(duelInfo.getRightUnit() != null){
            model.addAttribute("rightUnit", duelInfo.getRightUnit().getName());
        }
        model.addAttribute("side", side);

        //System.out.println(duelInfo.getLeftQuantity());
        //model.addAttribute("leftQuantity", duelInfo.getLeftQuantity());
        //model.addAttribute("rightQuantity", duelInfo.getRightQuantity());
    }

    public void loadUnit(Model model, DuelInfo duelInfo, String side, String name){
        Unit unit = unitRepository.getReferenceById(name);

        if(side.equals("left")){
            model.addAttribute("leftUnit", unit);
            model.addAttribute("rightUnit", duelInfo.getRightUnit());
        }
        else if (side.equals("right")) {
            model.addAttribute("rightUnit", unit);
            model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        }

        //model.addAttribute("leftQuantity", duelInfo.getLeftQuantity());
    }

    public void loadBattle(Model model, DuelInfo duelInfo){
        model.addAttribute("leftUnit", duelInfo.getLeftUnit());
        model.addAttribute("rightUnit", duelInfo.getRightUnit());
        model.addAttribute("leftQuantity", duelInfo.getLeftQuantity());
        model.addAttribute("rightQuantity", duelInfo.getRightQuantity());
        model.addAttribute("leftHeroAttack", duelInfo.getLeftHeroAttack());
        model.addAttribute("leftHeroDefense", duelInfo.getLeftHeroDefense());
        model.addAttribute("rightHeroAttack", duelInfo.getRightHeroAttack());
        model.addAttribute("rightHeroDefense", duelInfo.getRightHeroDefense());
        battleService.prepareBattle(duelInfo);
    }
}
