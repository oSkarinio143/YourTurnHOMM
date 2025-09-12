package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.domain.port.repository.UnitRepository;
import pl.oskarinio.yourturnhomm.infrastructure.port.communication.Communication;

public class DuelUseCase {
    private final UnitRepository unitRepository;
    private final Battle battle;
    private final Communication communication;

    public DuelUseCase(UnitRepository unitRepository, Battle battle, Communication communication) {
        this.unitRepository = unitRepository;
        this.battle = battle;
        this.communication = communication;
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
        return communication.createUserUUID();
    }

    public void closeEmitter(String userUUID){
        communication.closeConnection(userUUID);
    }
}
