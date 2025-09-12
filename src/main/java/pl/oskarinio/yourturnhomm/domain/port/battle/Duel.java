package pl.oskarinio.yourturnhomm.domain.port.battle;

import pl.oskarinio.yourturnhomm.domain.model.battle.Side;
import pl.oskarinio.yourturnhomm.domain.model.form.DuelForm;

public interface Duel {
    void loadBattle(DuelForm duelForm);
    void loadUnit(DuelForm duelForm, Side side, String tempUnitName);
}
