package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.port.UnitRepository;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;
import pl.oskarinio.yourturnhomm.infrastructure.temp.CommunicationUseCase;

public class DuelService {
    private final UnitRepository unitRepository;
    private final Battle battle;
    private final CommunicationUseCase communicationUseCase;

    public DuelService(UnitRepository unitRepository, Battle battle, CommunicationUseCase communicationUseCase) {
        this.unitRepository = unitRepository;
        this.battle = battle;
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
        battle.prepareBattle(duelForm);
    }

    public String getUserUUID(){
        return communicationUseCase.createUserUUID();
    }

    public void closeEmitter(String userUUID){
        communicationUseCase.closeConnection(userUUID);
    }
}
