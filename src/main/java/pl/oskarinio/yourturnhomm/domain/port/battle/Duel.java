package pl.oskarinio.yourturnhomm.domain.port.in.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;

public interface DuelUseCase {
    void loadBattle(DuelForm duelForm);
    void loadUnit(DuelForm duelForm, Side side, String tempUnitName);
}
