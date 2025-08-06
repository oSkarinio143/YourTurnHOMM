package oskarinio143.yourturnhomm.service;

import org.springframework.stereotype.Service;
import oskarinio143.yourturnhomm.model.constant.Side;
import oskarinio143.yourturnhomm.model.form.DuelForm;
import oskarinio143.yourturnhomm.model.entity.Unit;
import oskarinio143.yourturnhomm.repository.UnitRepository;

@Service
public class DuelService {
    private final UnitRepository unitRepository;
    private final BattleService battleService;
    private final CommunicationService communicationService;

    public DuelService(UnitRepository unitRepository, BattleService battleService, CommunicationService communicationService) {
        this.unitRepository = unitRepository;
        this.battleService = battleService;
        this.communicationService = communicationService;
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

    public String getUserUUID(){
        return communicationService.createUserUUID();
    }

    public void closeEmitter(String userUUID){
        communicationService.closeConnection(userUUID);
    }
}
