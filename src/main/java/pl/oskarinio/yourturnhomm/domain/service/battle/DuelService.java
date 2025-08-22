package pl.oskarinio.yourturnhomm.domain.service.battle;

import pl.oskarinio.yourturnhomm.app.battle.port.in.BattleUseCase;
import pl.oskarinio.yourturnhomm.app.battle.port.in.CommunicationUseCase;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.app.battle.port.out.UnitRepositoryPort;
import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.DuelForm;

public class DuelService {
    private final UnitRepositoryPort unitRepositoryPort;
    private final BattleUseCase battleUseCase;
    private final CommunicationUseCase communicationUseCase;

    public DuelService(UnitRepositoryPort unitRepositoryPort, BattleUseCase battleUseCase, CommunicationUseCase communicationUseCase) {
        this.unitRepositoryPort = unitRepositoryPort;
        this.battleUseCase = battleUseCase;
        this.communicationUseCase = communicationUseCase;
    }

    public void loadUnit(DuelForm duelForm, Side side, String tempUnitName){
        if(side != null) {
            Unit unit = unitRepositoryPort.getReferenceById(tempUnitName);
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
