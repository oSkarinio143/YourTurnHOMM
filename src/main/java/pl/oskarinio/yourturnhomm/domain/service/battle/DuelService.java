package pl.oskarinio.yourturnhomm.domain.service.battle;

import pl.oskarinio.yourturnhomm.app.battle.port.in.BattleUseCase;
import pl.oskarinio.yourturnhomm.app.battle.port.in.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.domain.model.entity.Unit;
import pl.oskarinio.yourturnhomm.app.database.port.out.UnitRepository;

public class DuelService {
    private final UnitRepository unitRepository;
    private final BattleUseCase battleUseCase;
    private final CommunicationUseCase communicationUseCase;

    public DuelService(UnitRepository unitRepository, BattleUseCase battleUseCase, CommunicationUseCase communicationUseCase) {
        this.unitRepository = unitRepository;
        this.battleUseCase = battleUseCase;
        this.communicationUseCase = communicationUseCase;
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
        battleUseCase.prepareBattle(duelForm);
    }

    public String getUserUUID(){
        return communicationUseCase.createUserUUID();
    }

    public void closeEmitter(String userUUID){
        communicationUseCase.closeConnection(userUUID);
    }
}
