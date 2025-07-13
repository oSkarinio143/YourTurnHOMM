package oskarinio143.heroes3.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import oskarinio143.heroes3.model.constant.Side;
import oskarinio143.heroes3.model.form.DuelForm;
import oskarinio143.heroes3.model.entity.Unit;
import oskarinio143.heroes3.repository.UnitRepository;

import java.util.List;

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

    public void loadUnit(DuelForm duelForm, Side side, String tempUnitName){
        if(side != null) {
            Unit unit = unitRepository.getReferenceById(tempUnitName);
            if (side == Side.LEFT)
                duelForm.setLeftUnit(unit);
            if (side == Side.RIGHT)
                duelForm.setRightUnit(unit);
        }
    }

    public void loadBattle(DuelForm duelForm){
        battleService.prepareBattle(duelForm);
    }
}
