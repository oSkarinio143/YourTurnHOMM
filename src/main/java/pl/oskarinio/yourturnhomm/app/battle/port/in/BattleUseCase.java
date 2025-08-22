package pl.oskarinio.yourturnhomm.app.battle.port.in;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.form.DuelForm;

public interface BattleUseCase {
    void prepareBattle(DuelForm duelForm);
}
