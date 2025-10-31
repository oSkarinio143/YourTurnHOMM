package pl.oskarinio.yourturnhomm.domain.usecase.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.battle.Unit;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;
import pl.oskarinio.yourturnhomm.domain.port.battle.Battle;
import pl.oskarinio.yourturnhomm.domain.port.out.UnitRepository;

public class DuelUseCase {
    private final UnitRepository unitRepository;
    private final Battle battle;

    public DuelUseCase(UnitRepository unitRepository, Battle battle) {
        this.unitRepository = unitRepository;
        this.battle = battle;
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
        System.out.println("Laduje Battla");
        System.out.println("zmapowany UUID - " + duelForm.getUserUUID());
        battle.prepareBattle(duelForm);
    }
}
