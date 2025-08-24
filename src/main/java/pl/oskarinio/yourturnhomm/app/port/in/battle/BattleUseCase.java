package pl.oskarinio.yourturnhomm.app.port.in.battle;

import pl.oskarinio.yourturnhomm.infrastructure.adapter.in.model.DuelForm;

public interface BattleUseCase {
    void prepareBattle(DuelForm duelForm);
}
